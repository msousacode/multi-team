package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

        UUID patientId,
        String namePatient,
        List<ProfessionalInTreatment> treatments
) {

    private TreatmentResponse(Treatment treatment, List<ProfessionalInTreatment> list) {
        this(
                treatment.getId(),
                treatment.getSituation(),
                treatment.getInitialDate(),
                treatment.getFinalDate(),
                treatment.getPatient().getDateBirth(),
                treatment.getPatient().getId(),
                treatment.getPatient().getName(),
                list
        );
    }

    public record ProfessionalInTreatment(
            UUID treatmentId,
            UUID professionalId,
            String nameProfessional,
            String specialty,
            SituationEnum situation
    ) {

    }
}
