package com.multiteam.core.security;

import com.multiteam.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private long jwtExpiration;

    private SecretKey secretKey;

    private final UserService userService;

    public TokenProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void setUpSecretKey() {
        var secret = Base64.getEncoder().encodeToString(this.tokenSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //var ownerId = userService.getOwnerId(userPrincipal.getId());

        Date expiryDate = Date.from(Instant.now().plus(Duration.ofSeconds(jwtExpiration)));

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("_ow", "")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(this.secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims body = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();

        String username = body.getSubject();
        List<GrantedAuthority> authorities = getRoles(body);

        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }

    private List<GrantedAuthority> getRoles(Claims body) {
        Collection<?> roles = body.get("roles", Collection.class);
        if (roles != null) {
            return roles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException ex) {
        }
        return false;
    }

    public Map<String, String> openToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        String ownerId = claims.get("_ow", String.class);

        Assert.notNull(userId, "userId can not be null");

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("ownerId", ownerId);

        return userInfo;
    }
}
