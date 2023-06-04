package com.multiteam.modules.annotation;

import java.util.UUID;

public record AnnotationDTO(
        UUID treatmentId,
        String annotation
) {
}
