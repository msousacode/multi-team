package com.multiteam.modules.anamnese.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.anamnese.Anamnese;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.modules.patient.dto.PatientDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnamneseResponse(
        UUID id,
        String annotation,
        String conclusion,
        AnamneseEnum status,
        boolean active,
        @JsonFormat(pattern="dd/MM/yyyy")
        LocalDateTime createDate,
        PatientDTO patientDTO
) {
    public AnamneseResponse(Anamnese anamnese) {
        this(
                anamnese.getId(),
                anamnese.getAnnotation(),
                anamnese.getConclusion(),
                anamnese.getStatus(),
                anamnese.isActive(),
                anamnese.getCreatedDate(),
                PatientDTO.fromPatientDTO(anamnese.getPatient())
        );
    }
}
