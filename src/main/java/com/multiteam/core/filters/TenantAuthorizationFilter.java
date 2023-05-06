package com.multiteam.core.filters;

import com.multiteam.core.authentication.TenantAuthenticationToken;
import com.multiteam.core.service.JwtService;
import com.multiteam.core.utils.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TenantAuthorizationFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JwtService jwtService;

    public TenantAuthorizationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);

        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith(Constants.BEARER_SCHEMA)) {
            TenantAuthenticationToken tenantAuthenticationToken = getTenantAuthenticationToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private TenantAuthenticationToken getTenantAuthenticationToken(String authorizationHeader) {
        TenantAuthenticationToken tenantAuthenticationToken = null;

        try {
            /* AUTHENTICATION */
            Jws<Claims> jwsClaims = jwtService.parseJwt(authorizationHeader);
            String email = jwsClaims.getBody().getSubject();
            /* AUTHORIZATION */
            List<GrantedAuthority> authorities = getSimpleGrantedAuthorities(jwsClaims);
            String tenantId = jwsClaims.getBody().get(Constants.TENANT_ID_CLAIM, String.class);
            tenantAuthenticationToken = new TenantAuthenticationToken(email, authorities, UUID.fromString(tenantId));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("Problems with JWT", e);
        }

        return tenantAuthenticationToken;
    }

    private List<GrantedAuthority> getSimpleGrantedAuthorities(Jws<Claims> jwsClaims) {
        Collection<?> roles = jwsClaims.getBody().get("roles", Collection.class);
        if(roles != null) {
            return roles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}