package com.multiteam.controller.dto.request;

import com.multiteam.enums.SexEnum;

import java.util.UUID;

public record PatientRequest(
        UUID id,
        String name,
        String middleName,
        SexEnum sex,
        Integer age,
        Integer months,
        String internalObservation,
        String externalObservation,
        boolean active,
        UUID clinicId
) {
}
