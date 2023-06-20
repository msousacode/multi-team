package com.multiteam.modules.treatment.dto;

import java.util.UUID;

public record TreatmentSearch(
        UUID patientId,
        String patientName
) {
}
