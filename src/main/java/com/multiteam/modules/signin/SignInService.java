package com.multiteam.modules.signin;

import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.core.models.EmailVO;
import com.multiteam.core.security.CustomAuthenticationManager;
import com.multiteam.core.service.EmailService;
import com.multiteam.core.service.JwtService;
import com.multiteam.modules.role.Role;
import com.multiteam.modules.role.RoleRepository;
import com.multiteam.modules.signin.payload.SignInDTO;
import com.multiteam.modules.signin.payload.SignUpDTO;
import com.multiteam.modules.signin.payload.TokenDTO;
import com.multiteam.modules.user.User;
import com.multiteam.modules.user.UserRepository;
import com.sendgrid.helpers.mail.objects.Content;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class SignInService {

    private final static Logger logger = LogManager.getLogger(SignInService.class);

    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public SignInService(
            final CustomAuthenticationManager customAuthenticationManager,
            final JwtService jwtService,
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final EmailService emailService) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    public String signInUser(final SignInDTO loginRequest) {

        var authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        return jwtService.createJwt(authentication);
    }

    @Transactional
    public void signUpUser(final SignUpDTO signUpDTO) {

        if (userRepository.existsByEmail(signUpDTO.email())) {
            throw new BadRequestException("Email address already in use.");
        }

        Role role = roleRepository.findByRole(RoleEnum.ROLE_OWNER);
        signUpDTO.roles().add(role);

        UUID provisionalTenantId = UUID.fromString("61a9c194-386d-46e1-ab9d-d26d6d50a1fc");

        var builder = new User.Builder(
                null, provisionalTenantId, signUpDTO.name(), signUpDTO.email(), true)
                .provider(AuthProviderEnum.local)
                .password(new BCryptPasswordEncoder().encode(signUpDTO.password()))
                .roles(signUpDTO.roles())
                .userType(UserEnum.OWNER)
                .build();

        var result = userRepository.save(builder);
        userRepository.updateTenantIdMyUser(result.getId(), result.getId());
    }

    public TokenDTO checkToken(final String token) {
        logger.info("check token {}", token);
        var userInfo = jwtService.openToken(token);

        logger.info("valid token {}", token);
        return new TokenDTO(userInfo.get("userId"));
    }

    @Transactional
    public Boolean forget(String email) {
        var user = userRepository.findByEmail(email.trim());

        if (user.isEmpty()) {
            return Boolean.FALSE;
        }
        String passwordProvisional = UUID.randomUUID().toString().substring(0, 10);

        user.get().setPassword(new BCryptPasswordEncoder().encode(passwordProvisional));

        userRepository.save(user.get());

        logger.info("Password of user {} been changed by forget action", email);

        String subject = getSubject();
        Content content = getContent(user.get(), passwordProvisional);

        var emailVO = EmailVO.buildEmail(user.get(), subject, content);

        if (emailService.sendEmailNewUser(emailVO)) {
            logger.warn("password was updated, but an error occurred when sending the first login email: {}", email);
        }

        return Boolean.TRUE;
    }

    private static String getSubject() {
        return "Team Clinic | Recuperação de acesso";
    }

    private static Content getContent(User user, String passwordProvisional) {
        return new Content("text/plain",
                """
                     Vi que você está com problemas para se autenticar na plataforma e pediu uma restauração de senha. 
                     Caso queira prosseguir com esta requisição, basta realizar o seu login utilizando a senha provisória abaixo. 
                     
                     Para logar no Portal, utilize as credenciais:
                     E-mail: %s
                     Senha: %s
                """.formatted(user.getEmail(), passwordProvisional));
    }
}
