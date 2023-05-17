package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.clinic.ClinicDTO;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TreatmentEditResponse(
        UUID id,
        Set<ClinicDTO> clinics,
        Set<UUID> professionals,
        SituationEnum situation,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate initialDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate finalDate,
        String observation
) {
    private TreatmentEditResponse(Treatment treatment, Set<Clinic> clinics, Set<UUID> professionals) {
        this(
                treatment.getId(),
                clinics.stream().map(ClinicDTO::new).collect(Collectors.toSet()),
                professionals,
                treatment.getSituation(),
                treatment.getInitialDate(),
                treatment.getFinalDate(),
                treatment.getDescription()
        );
    }

    public static TreatmentEditResponse fromTreatmentEditResponse(Treatment treatment, Set<Clinic> clinics, Set<UUID> professionals) {
        return new TreatmentEditResponse(treatment, clinics, professionals);
    }
}
