package com.multiteam.controller.dto;

import java.util.Set;
import java.util.UUID;

public record ProfessionalDTO(
        UUID id,
        String name,
        String specialty,
        String cellPhone,
        String email,
        Set<String> clinicId,
        UUID ownerId
) {
}