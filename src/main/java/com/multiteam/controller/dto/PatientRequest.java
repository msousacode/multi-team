package com.multiteam.controller.dto;

import com.multiteam.persistence.enums.SexType;

import java.util.UUID;

public record PatientRequest(
        UUID id,
        String name,
        String middleName,
        SexType sex,
        Integer age,
        Integer months,
        String internalObservation,
        String externalObservation,
        boolean active,
        UUID clinicId
) {
}
