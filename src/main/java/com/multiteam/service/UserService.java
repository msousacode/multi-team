package com.multiteam.service;

import com.multiteam.enums.AuthProviderEnum;
import com.multiteam.exception.OwnerException;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static com.multiteam.enums.AuthProviderEnum.local;

@Service
public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getOwnerById(UUID userId) {
        return userRepository.findById(userId);
    }

    public UUID getOwnerId(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new OwnerException("ownerId not found")).getOwnerId();
    }

    @Transactional
    public User createUser(String name, String email, UUID ownerId, AuthProviderEnum providerEnum) {
        logger.info("create user email: {}, ownerId: {}", email, ownerId);
        var user = new User.Builder(null, name, email, true, ownerId).provider(providerEnum).build();
        return userRepository.save(user);
    }
}
