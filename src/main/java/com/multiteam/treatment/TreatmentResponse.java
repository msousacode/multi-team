package com.multiteam.treatment;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.patient.PatientDTO;

import java.time.LocalDate;
import java.util.UUID;

public record TreatmentResponse(
        UUID id,
        String description,
        SituationEnum situation,
        PatientDTO patientDTO,
        LocalDate initialDate,
        LocalDate finalDate
) {
    private TreatmentResponse(Treatment treatment) {
        this(
                treatment.getId(),
                treatment.getDescription(),
                treatment.getSituation(),
                PatientDTO.fromPatientDTO(treatment.getPatient()),
                treatment.getInitialDate(),
                treatment.getFinalDate()
        );
    }

    public static TreatmentResponse fromTreatmentResponse(Treatment treatment) {
        return new TreatmentResponse(treatment);
    }
}
