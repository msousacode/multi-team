package com.multiteam.core.configuration;

import com.multiteam.core.filters.TenantAuthorizationFilter;
import com.multiteam.core.security.CustomAuthenticationManager;
import com.multiteam.core.security.oauth2.CustomOAuth2UserService;
import com.multiteam.core.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.multiteam.core.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.multiteam.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.multiteam.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    private CustomAuthenticationManager authenticationManager;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /*
    By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
    the authorization request. But, since our service is stateless, we can't save it in
    the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService) throws Exception {

        //@formatter:off
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                //.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/oauth2/**").permitAll()
                .antMatchers(POST, "/v1/auth/sign-in").permitAll()
                .antMatchers(POST, "/v1/auth/sign-up").permitAll()
                .antMatchers(POST, "/v1/auth/info-token").permitAll()
                .antMatchers(POST, "/v1/auth/check-token").permitAll()
                .antMatchers(POST, "/v1/auth/forgot").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .authenticationManager(authenticationManager);// authentication application

        // Add our custom Token based authentication filter
        return http.addFilterBefore(new TenantAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class).build();
    }
}
