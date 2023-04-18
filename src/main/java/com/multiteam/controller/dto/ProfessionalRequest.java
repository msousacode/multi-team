package com.multiteam.controller.dto;

import com.multiteam.persistence.types.SpecialtyType;

import java.util.UUID;

public record ProfessionalRequest(
        UUID id,
        String name,
        String middleName,
        SpecialtyType specialty,
        String cellPhone,
        String email,
        UUID clinicId
) {
}
