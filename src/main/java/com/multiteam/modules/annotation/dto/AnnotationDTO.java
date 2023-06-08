package com.multiteam.modules.annotation.dto;

import com.multiteam.modules.annotation.Annotation;

import java.util.UUID;

public record AnnotationDTO(
        UUID treatmentId,
        String annotation
) {
    public AnnotationDTO(Annotation annotation) {
        this(
                annotation.getId(),
                annotation.getAnnotation()
        );
    }
}
