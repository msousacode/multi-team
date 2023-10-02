package com.multiteam.modules.patient.controller.dto;

import java.util.UUID;

public record PatientFilter(
        String patientName,
        UUID professionalId
) {
}
