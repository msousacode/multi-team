package com.multiteam.controller.dto;

import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        Boolean active,
        Set<String> roles
) {}
