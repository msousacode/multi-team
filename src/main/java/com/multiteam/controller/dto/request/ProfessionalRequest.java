package com.multiteam.controller.dto.request;

import com.multiteam.enums.SpecialtyEnum;

import java.util.UUID;

public record ProfessionalRequest(
        UUID id,
        String name,
        String middleName,
        SpecialtyEnum specialty,
        String cellPhone,
        String email,
        UUID clinicId
) {
}
