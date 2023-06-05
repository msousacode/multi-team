package com.multiteam.modules.annotation.dto;

import java.util.UUID;

public record AnnotationDTO(
        UUID treatmentId,
        String annotation
) {
}
