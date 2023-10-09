package com.multiteam.modules.treatment.dto;

import java.util.UUID;

public record TreatmentSearchDTO(
        UUID patientId,
        String patientName
) {
}
