package com.multiteam.service;

import com.multiteam.controller.dto.payload.LoginRequest;
import com.multiteam.controller.dto.payload.SignUpRequest;
import com.multiteam.controller.dto.response.CheckTokenResponse;
import com.multiteam.exception.BadRequestException;
import com.multiteam.persistence.entity.Role;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.RoleRepository;
import com.multiteam.persistence.repository.UserRepository;
import com.multiteam.enums.AuthProviderEnum;
import com.multiteam.enums.RoleEnum;
import com.multiteam.security.CustomAuthenticationManager;
import com.multiteam.security.TokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final CustomAuthenticationManager customAuthenticationManager;
    private final TokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthService(
            CustomAuthenticationManager customAuthenticationManager,
            TokenProvider jwtTokenProvider,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public String authenticationUser(LoginRequest loginRequest) {

        var authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createToken(authentication);
    }

    public Boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public void registerUser(SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.email())) {
            throw new BadRequestException("Email address already in use.");
        }

        Role role = roleRepository.findByRole(RoleEnum.ROLE_OWNER);
        signUpRequest.roles().add(role);

        var builder = new User.Builder(
                null, signUpRequest.name(), signUpRequest.email(), true, null)
                .provider(AuthProviderEnum.local)
                .password(new BCryptPasswordEncoder().encode(signUpRequest.password()))
                .roles(signUpRequest.roles())
                .build();

        var userResult = userRepository.save(builder);
        userRepository.updateOwnerId(userResult.getId());
    }

    public CheckTokenResponse checkToken(final String token) {
        logger.info("check token {}", token);
        var userInfo = jwtTokenProvider.openToken(token);

        if(jwtTokenProvider.validateToken(token)) {
            logger.info("valid token {}", token);
            return new CheckTokenResponse(userInfo.get("userId"), userInfo.get("ownerId"), true);

        } else {
            logger.error("invalid token {}", token);
            logger.debug("could not validate user token id {}", userInfo.get("userId"));
            return new CheckTokenResponse(null, null, false);
        }
    }
}
