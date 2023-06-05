package com.multiteam.modules.annotation.dto;

import java.util.UUID;

public record AnnototionSearch(
        UUID treatmentId,
        UUID professionalId,
        UUID patientId
) {
}
