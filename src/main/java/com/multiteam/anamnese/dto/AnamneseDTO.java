package com.multiteam.anamnese.dto;

import com.multiteam.anamnese.Anamnese;
import com.multiteam.core.enums.AnamneseEnum;

import java.util.UUID;

public record AnamneseDTO(
        UUID id,
        String annotation,
        String conclusion,
        AnamneseEnum status,
        UUID patientId,
        boolean active
) {
    public AnamneseDTO(Anamnese anamnese) {
        this(
                anamnese.getId(),
                anamnese.getAnnotation(),
                anamnese.getConclusion(),
                anamnese.getStatus(),
                anamnese.getPatient().getId(),
                anamnese.isActive()
        );
    }
}
