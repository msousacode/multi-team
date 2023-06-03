package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.treatment.TreatmentProfessional;

import java.time.LocalDateTime;
import java.util.UUID;

public record TreatmentAnnotationDTO(
        UUID treatmentProfessionalId,
        String professionalName,
        String specialty,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime createdDate
) {
    public TreatmentAnnotationDTO(TreatmentProfessional treatmentProfessional) {
        this(
                treatmentProfessional.getId(),
                treatmentProfessional.getProfessional().getName(),
                treatmentProfessional.getProfessional().getSpecialty().getName(),
                treatmentProfessional.getCreatedDate()
        );
    }
}
