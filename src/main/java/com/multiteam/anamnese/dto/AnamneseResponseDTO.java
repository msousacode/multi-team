package com.multiteam.anamnese.dto;

import com.multiteam.anamnese.Anamnese;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.patient.PatientDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AnamneseResponseDTO(
        UUID id,
        String annotation,
        String conclusion,
        AnamneseEnum status,
        boolean active,
        LocalDateTime createDate,
        PatientDTO patientDTO
) {
    public AnamneseResponseDTO(Anamnese anamnese) {
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
