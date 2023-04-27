package com.multiteam.controller.dto.request;

import java.util.Set;
import java.util.UUID;

public record ProfessionalRequest(
        UUID id,
        String name,
        String middleName,
        String specialty,
        String cellPhone,
        String email,
        Set<String> clinicId
) {
}
