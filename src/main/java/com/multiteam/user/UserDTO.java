package com.multiteam.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        Boolean active,
        Set<String> roles
) {
    public static UserDTO fromUserDTO(User user) {
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(i -> roles.add(i.getId().toString()));
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getActive(), roles);
    }
}
