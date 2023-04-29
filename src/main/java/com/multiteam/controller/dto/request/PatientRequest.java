package com.multiteam.controller.dto.request;

import com.multiteam.enums.SexEnum;

import java.time.LocalDate;
import java.util.UUID;

public record PatientRequest(
        UUID id,
        String name,
        SexEnum sex,
        Integer age,
        String privateObservation,
        String publicObservation,
        boolean active,
        UUID ownerId,
        LocalDate dateBirth
) {
}
