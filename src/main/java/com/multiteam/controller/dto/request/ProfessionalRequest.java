package com.multiteam.controller.dto.request;

import com.multiteam.persistence.enums.SpecialtyType;

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
