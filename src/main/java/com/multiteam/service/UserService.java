package com.multiteam.service;

import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getOwnerById(UUID userId) {
        return userRepository.findById(userId);
    }
}
