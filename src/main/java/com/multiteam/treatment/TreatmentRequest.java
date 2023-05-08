package com.multiteam.treatment;

import com.multiteam.core.enums.SituationEnum;

import java.time.LocalDate;
import java.util.UUID;

public record TreatmentRequest(
        UUID id,
        String description,
        SituationEnum situation,
        UUID patientId,
        UUID professionalId,
        UUID clinicId,
        LocalDate initialDate,
        LocalDate finalDate
) {
}
