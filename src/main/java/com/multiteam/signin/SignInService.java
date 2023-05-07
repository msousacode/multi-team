package com.multiteam.signin;

import com.multiteam.signin.dto.SignInDTO;
import com.multiteam.signin.dto.SignUpDTO;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.core.service.JwtService;
import com.multiteam.role.Role;
import com.multiteam.user.User;
import com.multiteam.role.RoleRepository;
import com.multiteam.user.UserRepository;
import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.security.CustomAuthenticationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class SignInService {

    private static final Logger logger = LogManager.getLogger(SignInService.class);

    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SignInService(
            CustomAuthenticationManager customAuthenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public String signInUser(SignInDTO loginRequest) {

        var authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        return jwtService.createJwt(authentication);
    }

    @Transactional
    public void signUpUser(SignUpDTO signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.email())) {
            throw new BadRequestException("Email address already in use.");
        }

        Role role = roleRepository.findByRole(RoleEnum.ROLE_OWNER);
        signUpRequest.roles().add(role);

        UUID provisionalTenantId = UUID.fromString("61a9c194-386d-46e1-ab9d-d26d6d50a1fc");

        var builder = new User.Builder(
                null, provisionalTenantId, signUpRequest.name(), signUpRequest.email(), true)
                .provider(AuthProviderEnum.local)
                .password(new BCryptPasswordEncoder().encode(signUpRequest.password()))
                .roles(signUpRequest.roles())
                .build();

        var result = userRepository.save(builder);
        userRepository.updateTenantIdMyUser(result.getId(), result.getId());
    }
/*
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
    }*/
}
