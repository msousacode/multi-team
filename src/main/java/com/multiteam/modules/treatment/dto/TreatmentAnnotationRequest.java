package com.multiteam.modules.treatment.dto;

import java.util.UUID;

public record TreatmentAnnotationRequest(
        UUID patientId,
        UUID treatmentId,
        String annotation
) {
}
