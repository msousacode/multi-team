package com.multiteam.modules.treatment.dto;

import com.multiteam.modules.treatment.TreatmentProfessional;

import java.util.UUID;

public record TreatmentProfessionalAnnotationDTO(
        UUID treatmentId,
        String annotation
) {
    public TreatmentProfessionalAnnotationDTO(TreatmentProfessional treatmentProfessional) {
        this(
                treatmentProfessional.getId(),
                treatmentProfessional.getAnnotation()
        );
    }
}
