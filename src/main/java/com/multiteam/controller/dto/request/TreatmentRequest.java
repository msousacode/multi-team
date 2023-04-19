package com.multiteam.controller.dto.request;

import com.multiteam.enums.SituationEnum;
import com.multiteam.enums.TreatmentEnum;

import java.time.LocalDate;
import java.util.UUID;

public record TreatmentRequest(
        UUID id,
        String description,
        TreatmentEnum treatmentType,
        SituationEnum situation,
        UUID patientId,
        UUID professionalId,
        UUID clinicId,
        LocalDate initialDate
) {
}
