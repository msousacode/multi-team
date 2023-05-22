package com.multiteam.modules.clinic.dto;

import com.multiteam.modules.clinic.Clinic;

import java.util.UUID;

public record ClinicUseInCacheResponse(
        UUID id,
        String clinicName
) {
    public ClinicUseInCacheResponse(Clinic clinic) {
        this(
                clinic.getId(),
                clinic.getClinicName()
        );
    }
}
