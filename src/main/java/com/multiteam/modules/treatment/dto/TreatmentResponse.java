package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.util.UUID;

public record TreatmentResponse(
        UUID id,
        SituationEnum situation,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate initialDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate finalDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dateBirth,
        String namePatient
) {
    private TreatmentResponse(Treatment treatment) {
        this(
                treatment.getId(),
                treatment.getSituation(),
                treatment.getInitialDate(),
                treatment.getFinalDate(),
                treatment.getPatient().getDateBirth(),
                treatment.getPatient().getName()
        );
    }

    public static TreatmentResponse fromTreatmentResponse(Treatment treatment) {
        return new TreatmentResponse(treatment);
    }
}
