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
                treatment.getPatient().getName(),
                list
        );
    }

    public static TreatmentResponse fromTreatmentResponse(Treatment treatment) {
        List<ProfessionalInTreatment> list = new ArrayList<>();
        treatment.getTreatmentProfessionals().forEach(i -> list.add(new ProfessionalInTreatment(i.getProfessional().getName(), i.getTreatment().getSituation())));
        return new TreatmentResponse(treatment, list);
    }

    public record ProfessionalInTreatment(
            String nameProfessional,
            SituationEnum situation
    ) {
    }
}
