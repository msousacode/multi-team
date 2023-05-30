package com.multiteam.modules.anamnese.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.modules.anamnese.Anamnese;
import com.multiteam.modules.patient.dto.PatientDTO;
import com.multiteam.modules.user.UserDTO;

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
        PatientDTO patientDTO,
        UserDTO creator
) {
    public AnamneseResponse(Anamnese anamnese, UserDTO creator) {
        this(
                anamnese.getId(),
                anamnese.getAnnotation(),
                anamnese.getConclusion(),
                anamnese.getStatus(),
                anamnese.isActive(),
                anamnese.getCreatedDate(),
                PatientDTO.fromPatientDTO(anamnese.getPatient()),
                creator
        );
    }
}
