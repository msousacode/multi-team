package com.multiteam.modules.guest.dto;

import java.util.UUID;

public record GuestPostDTO(
        UUID id,
        String name,
        String email,
        String cellPhone,
        Integer relationship
) {
}
