package com.multiteam.user;

import com.multiteam.core.contexts.TenantContext;
import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.role.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantContext tenantContext;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, TenantContext tenantContext) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tenantContext = tenantContext;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByTenantIdAndActiveIsTrue(tenantContext.getTenantId(), pageable).map(UserDTO::fromUserDTO);
    }

    @Transactional
    public User createUser(String name, String email, AuthProviderEnum providerEnum) {

        var password = UUID.randomUUID().toString().substring(0, 10);

        var role1 = roleRepository.findByRole(RoleEnum.ROLE_GUEST);
        var role2 = roleRepository.findByRole(RoleEnum.PERM_TREATMENT_READ);
        var role3 = roleRepository.findByRole(RoleEnum.PERM_SCHEDULE_READ);

        var user = new User.Builder(null, tenantContext.getTenantId(), name, email, true)
                .roles(Set.of(role1, role2, role3))
                .provider(providerEnum)
                .password(new BCryptPasswordEncoder().encode(password))
                .build();
        user.setProvisionalPassword(password);
        return userRepository.save(user);
    }

    public Optional<UserDTO> getUser(UUID userId) {

        var user = userRepository.findByTenantIdAndId(tenantContext.getTenantId(), userId);

        if (user.isEmpty()) {
            logger.warn("user not found userId {}", userId);
            return Optional.empty();
        }

        return user.map(UserDTO::fromUserDTO);
    }

    @Transactional
    public Boolean updateUser(UserDTO userDTO) {

        var userResult = userRepository.findById(userDTO.id());

        if (userResult.isEmpty()) {
            logger.warn("user not found with userId: {}", userDTO.id());
            return Boolean.FALSE;
        }

        var rolesIds = userDTO.roles().stream().map(UUID::fromString).toList();

        var roles = roleRepository.findAllById(rolesIds);
        var rolesSet = Set.copyOf(roles);

        var builder = new User.Builder(
                userDTO.id(),
                userResult.get().getTenantId(),
                userDTO.name(),
                userDTO.email(),
                userDTO.active())
                .roles(rolesSet)
                .build();

        userRepository.save(builder);

        logger.info("updated user: {} ", builder.toString());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean inactiveUser(UUID userId, UUID ownerId) {
        //userRepository.inactiveUser(userId, ownerId);
        logger.info("inactive userId: {}, ownerId: {}", userId, ownerId);
        return Boolean.TRUE;
    }
}
