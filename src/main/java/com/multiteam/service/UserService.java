package com.multiteam.service;

import com.multiteam.controller.dto.ProfessionalDTO;
import com.multiteam.controller.dto.UserDTO;
import com.multiteam.enums.AuthProviderEnum;
import com.multiteam.enums.RoleEnum;
import com.multiteam.exception.OwnerException;
import com.multiteam.persistence.entity.Role;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.RoleRepository;
import com.multiteam.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> getOwnerById(UUID userId) {
        return userRepository.findById(userId);
    }

    public UUID getOwnerId(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new OwnerException("ownerId not found")).getOwnerId();
    }

    public Page<UserDTO> getAllUsers(UUID ownerId, Pageable pageable) {
        return userRepository.findByOwnerIdAndActiveIsTrue(ownerId, pageable)
                .map(i -> new UserDTO(i.getId(), i.getName(), i.getEmail(), i.getActive(), Set.of()));
    }

    @Transactional
    public User createUser(String name, String email, UUID ownerId, AuthProviderEnum providerEnum) {
        logger.info("create user email: {}, ownerId: {}", email, ownerId);
        var password = UUID.randomUUID().toString().substring(0, 10);

        var role1 = roleRepository.findByRole(RoleEnum.ROLE_GUEST);
        var role2 = roleRepository.findByRole(RoleEnum.PERM_TREATMENT_READ);
        var role3 = roleRepository.findByRole(RoleEnum.PERM_SCHEDULE_READ);

        var user = new User.Builder(null, name, email, true, ownerId)
                .roles(Set.of(role1, role2, role3))
                .provider(providerEnum)
                .password(new BCryptPasswordEncoder().encode(password)).build();
        user.setProvisionalPassword(password);
        return userRepository.save(user);
    }

    public Optional<UserDTO> getUserByOwnerId(UUID userId, UUID ownerId) {

        var user = userRepository.findByIdAndOwnerId(userId, ownerId);

        if (user.isEmpty()) {
            logger.warn("user not found userId {}, ownerId {}", userId, ownerId);
            return Optional.empty();
        }

        Set<String> rolesIds = new HashSet<>();
        user.get().getRoles().forEach(i -> rolesIds.add(i.getId().toString()));

        return user.map(i -> new UserDTO(i.getId(), i.getName(), i.getEmail(), i.getActive(), rolesIds));
    }

    @Transactional
    public Boolean updateUser(UserDTO userDTO) {

        var userResult = userRepository.findById(userDTO.id());

        if(userResult.isEmpty()) {
            logger.warn("user not found with userId: {}", userDTO.id());
            return Boolean.FALSE;
        }

        var rolesIds = userDTO.roles().stream().map(UUID::fromString).toList();

        var roles = roleRepository.findAllById(rolesIds);
        var rolesSet = Set.copyOf(roles);

        var builder = new User.Builder(
                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.active(),
                userResult.get().getOwnerId())
                .roles(rolesSet)
                .build();

        userRepository.save(builder);

        logger.info("updated user: {} ", builder.toString());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean inactiveUser(UUID userId, UUID ownerId) {
        userRepository.inactiveUser(userId, ownerId);
        logger.info("inactive userId: {}, ownerId: {}", userId, ownerId);
        return Boolean.TRUE;
    }
}
