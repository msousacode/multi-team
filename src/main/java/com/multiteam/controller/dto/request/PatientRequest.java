package com.multiteam.controller.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record PatientRequest(
        UUID id,
        String name,
        String email,
        String cellPhone,
        String sex,
        Integer age,
        boolean active,
        UUID ownerId,
        LocalDate dateBirth
) {
}
