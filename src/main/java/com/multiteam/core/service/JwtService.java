package com.multiteam.core.service;

import com.multiteam.core.models.UserPrincipal;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.role.Role;
import com.multiteam.modules.user.User;
import com.multiteam.modules.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final static Logger logger = LogManager.getLogger(JwtService.class);

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpiration}")
    private Integer tokenExpired;

    private SecretKey secretKey;

    private JwtParser jwtParser;

    private final UserService userService;

    private final ProfessionalService professionalService;

    public JwtService(UserService userService, ProfessionalService professionalService) {
        this.userService = userService;
        this.professionalService = professionalService;
    }

    @PostConstruct
    private void init() {
        var secret = Base64.getEncoder().encodeToString(this.tokenSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String createJwt(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expiryDate = Date.from(Instant.now().plus(Duration.ofSeconds(tokenExpired)));

        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        ArrayList<String> authoritiesList = new ArrayList<>(authorities.size());

        for (GrantedAuthority authority : authorities) {
            authoritiesList.add(authority.getAuthority());
        }

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("roles", authoritiesList)
                .claim("tenantId", userPrincipal.getTenantId())
                .setExpiration(expiryDate)
                .signWith(this.secretKey)
                .compact();
    }

    public Jws<Claims> parseJwt(String authorizationHeader) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return jwtParser.parseClaimsJws(authorizationHeader);
    }

    public Map<String, String> openToken(String token) {
        var user = extractUserToken(token);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", user.getId().toString());
        userInfo.put("userName", user.getName());

        return userInfo;
    }

    private User extractUserToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        String userId = claims.getSubject();
        return userService.getUserById(UUID.fromString(userId));
    }

    public List<String> extractRoles(String token) {
        var user = extractUserToken(token);
        return user.getRoles().stream().map(role -> role.getRole().name()).toList();
    }

    public Boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return Boolean.FALSE;
    }
}
