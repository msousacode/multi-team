package com.multiteam.core.service;

import com.multiteam.core.models.UserPrincipal;
import com.multiteam.core.utils.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    private final static Logger logger = LogManager.getLogger(JwtService.class);

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    private SecretKey secretKey;

    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        var secret = Base64.getEncoder().encodeToString(this.tokenSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String createJwt(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expiryDate = Date.from(Instant.now().plus(Duration.ofSeconds(180000)));

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("roles", userPrincipal.getAuthorities())
                .claim("tenantId", userPrincipal.getTenantId())
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parseJwt(String authorizationHeader) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return jwtParser.parseClaimsJws(authorizationHeader.replace(Constants.BEARER_SCHEMA, ""));
    }

    public Map<String, String> openToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.get("_ui", String.class);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", userId);

        return userInfo;
    }
}
