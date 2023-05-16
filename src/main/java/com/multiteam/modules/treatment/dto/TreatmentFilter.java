package com.multiteam.modules.treatment.dto;

import java.util.UUID;

public record TreatmentFilter(
        UUID patientId,
        String patientName
) {
}
