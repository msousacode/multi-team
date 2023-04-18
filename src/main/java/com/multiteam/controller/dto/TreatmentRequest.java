package com.multiteam.controller.dto;

import com.multiteam.persistence.types.SituationType;
import com.multiteam.persistence.types.TreatmentType;

import java.time.LocalDate;
import java.util.UUID;

public record TreatmentRequest(
        UUID id,
        String description,
        TreatmentType treatmentType,
        SituationType situation,
        UUID patientId,
        UUID professionalId,
        UUID clinicId,
        LocalDate initialDate
) {
}
