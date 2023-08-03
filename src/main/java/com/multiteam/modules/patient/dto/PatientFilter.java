package com.multiteam.modules.patient.dto;

import java.util.UUID;

public record PatientFilter(
        String patientName,
        UUID professionalId
) {
}
