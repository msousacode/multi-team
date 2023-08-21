package com.multiteam.core.filters;

import com.multiteam.core.authentication.TenantAuthenticationToken;
import com.multiteam.core.exception.AuthenticationException;
import com.multiteam.core.service.JwtService;
import com.multiteam.core.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class TenantAuthorizationFilter extends GenericFilterBean {

  private final Logger log = LoggerFactory.getLogger(getClass());
  public static final String HEADER_PREFIX = "Bearer ";

  private final JwtService jwtService;

  public TenantAuthorizationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String authorizationHeader = resolveToken((HttpServletRequest) request);

    if (StringUtils.hasText(authorizationHeader)) {
      TenantAuthenticationToken tenantAuthenticationToken = getTenantAuthenticationToken(
          authorizationHeader);
      SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
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
      tenantAuthenticationToken = new TenantAuthenticationToken(email, authorities,
          UUID.fromString(tenantId));
    } catch (Exception e) {
      log.error("Problems with JWT", e);
      throw new AuthenticationException("Problems with JWT", e.getCause());
    }

    return tenantAuthenticationToken;
  }

  private List<GrantedAuthority> getSimpleGrantedAuthorities(Jws<Claims> jwsClaims) {
    Collection<?> roles = jwsClaims.getBody().get("roles", Collection.class);
    if (roles != null) {
      return roles.stream()
          .map(authority -> new SimpleGrantedAuthority(authority.toString()))
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }
}