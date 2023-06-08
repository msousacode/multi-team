package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.annotation.Annotation;

import java.time.LocalDateTime;
import java.util.UUID;

public record TreatmentAnnotationDTO(
        UUID treatmentProfessionalId,
        String professionalName,
        String specialty,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime createdDate
) {
    public TreatmentAnnotationDTO(Annotation annotation) {
        this(
                annotation.getId(),
                annotation.getTreatmentProfessional().getProfessional().getName(),
                annotation.getTreatmentProfessional().getProfessional().getSpecialty().getName(),
                annotation.getCreatedDate()
        );
    }
}
