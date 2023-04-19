package com.multiteam.util;

import com.multiteam.constants.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TokenUtil {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    private SecretKey secretKey;

    protected String defaultAccessToken;

    @BeforeEach
    public void init() {
        var secret = Base64.getEncoder().encodeToString(tokenSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        defaultAccessToken = generateToken(Constants.USER_OWNER_ADMIN);
    }

    public String generateToken(String email) {

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_OWNER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        ArrayList<String> authoritiesList = new ArrayList<>(authorities.size());

        for (GrantedAuthority authority : authorities) {
            authoritiesList.add(authority.getAuthority());
        }

        return Jwts.builder()
                .setSubject(Constants.USER_OWNER_ADMIN)
                .claim("roles", authoritiesList)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(864000000))))
                .signWith(this.secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
