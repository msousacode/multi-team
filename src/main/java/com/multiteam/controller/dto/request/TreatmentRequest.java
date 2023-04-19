package com.multiteam.controller.dto.request;

import com.multiteam.persistence.enums.SituationType;
import com.multiteam.persistence.enums.TreatmentType;

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
