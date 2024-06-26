package com.multiteam.modules.user;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.modules.role.Role;
import com.multiteam.modules.role.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public User createUser(UserDTO userDTO, AuthProviderEnum providerEnum) {

        var password = UUID.randomUUID().toString().substring(0, 10);

        List<UUID> rolesUUIDs = new ArrayList<>();

        if(userDTO.roles() == null) {
            rolesUUIDs.add(roleRepository.findByRole(RoleEnum.ROLE_SCHEDULE).getId());
        } else {
            rolesUUIDs = userDTO.roles().stream().map(roleId -> UUID.fromString(roleId)).toList();
        }

        var defaultRoles = roleRepository.findAllById(rolesUUIDs);

        var user = new User.Builder(null, tenantContext.getTenantId(), userDTO.name(), userDTO.email(), true)
                .roles(defaultRoles)
                .provider(providerEnum)
                .password(new BCryptPasswordEncoder().encode(password))
                .userType(UserEnum.USER)
                .build();

        user.setProvisionalPassword(password);

        return userRepository.save(user);
    }

    public User getUserById(final UUID userId) {
        return userRepository.getUserById(userId);
    }

    public Optional<UserDTO> getUser(final UUID userId) {

        var user = userRepository.findByTenantIdAndId(tenantContext.getTenantId(), userId);

        if (user.isEmpty()) {
            logger.warn("user not found userId {}", userId);
            return Optional.empty();
        }

        return user.map(UserDTO::fromUserDTO);
    }

    @CacheEvict(cacheNames = "roles", allEntries = true)
    @Transactional
    public Boolean updateUser(final UserDTO userDTO) {

        var user = userRepository.findById(userDTO.id());

        if (user.isEmpty()) {
            logger.warn("user not found with userId: {}", userDTO.id());
            return Boolean.FALSE;
        }

        var rolesIds = userDTO.roles().stream().map(UUID::fromString).toList();

        var roles = roleRepository.findAllById(rolesIds);

        user.get().setName(userDTO.name());
        user.get().setEmail(userDTO.email());
        user.get().setRoles(roles);

        userRepository.save(user.get());

        logger.info("updated user: {} ", user.toString());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean inactiveUser(final UUID userId) {
        userRepository.inactiveUser(userId, tenantContext.getTenantId());
        logger.info("inactive userId: {}", userId);
        return Boolean.TRUE;
    }

    @Transactional
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        if (userRepository.findOneById(passwordResetDTO.id()).isPresent()) {
            userRepository.updatePassword(passwordResetDTO.id(), new BCryptPasswordEncoder().encode(passwordResetDTO.password1()), tenantContext.getTenantId());
        } else {
            logger.error("user not found, userId: {}", passwordResetDTO.id());
        }
    }

    @Cacheable("roles")
    public List<Role> getRolesPermissions(final UUID userId) {
        return userRepository.findById(userId).map(User::getRoles).orElse(List.of());
    }
}
