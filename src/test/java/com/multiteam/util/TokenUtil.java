package com.multiteam.util;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.exception.BadRequestException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TokenUtil {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        var secret = Base64.getEncoder().encodeToString(this.tokenSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Collection<? extends GrantedAuthority> authorities) {

        ArrayList<String> authoritiesList = new ArrayList<>(authorities.size());

        for (GrantedAuthority authority : authorities) {
            authoritiesList.add(authority.getAuthority());
        }

        Date expiryDate = Date.from(Instant.now().plus(Duration.ofSeconds(180000)));

        return Jwts.builder()
                .setSubject(ConstantsToTests.OWNER_ID)
                .claim("roles", authoritiesList)
                .claim("tenantId", ConstantsToTests.TENANT_ID)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    protected String getToken(RoleEnum role) {

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

        if (role.equals(RoleEnum.ROLE_OWNER)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_OWNER"));

        } else if (role.equals(RoleEnum.ROLE_ADMIN)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        } else if (role.equals(RoleEnum.ROLE_SCHEDULE)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_SCHEDULE"));

        } else if (role.equals(RoleEnum.ROLE_PROFESSIONAL)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_PROFESSIONAL"));

        } else if (role.equals(RoleEnum.ROLE_PATIENT)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_PATIENT"));

        } else if (role.equals(RoleEnum.ROLE_GUEST)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_GUEST"));

        } else if (role.equals(RoleEnum.PERM_TREATMENT_WRITE)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("PERM_TREATMENT_WRITE"));

        } else if (role.equals(RoleEnum.PERM_TREATMENT_READ)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("PERM_TREATMENT_READ"));

        } else if (role.equals(RoleEnum.PERM_PATIENT_WRITE)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("PERM_PATIENT_WRITE"));

        } else if (role.equals(RoleEnum.PERM_PATIENT_READ)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("PERM_PATIENT_READ"));

        } else {
            throw new BadRequestException("role mandatory");
        }

        return generateToken(authorities);
    }
}
