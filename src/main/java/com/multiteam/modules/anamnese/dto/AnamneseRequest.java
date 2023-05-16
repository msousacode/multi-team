package com.multiteam.modules.anamnese.dto;

import com.multiteam.modules.anamnese.Anamnese;
import com.multiteam.core.enums.AnamneseEnum;

import java.util.UUID;

public record AnamneseRequest(
        UUID id,
        String annotation,
        String conclusion,
        AnamneseEnum status,
        UUID patientId,
        boolean active
) {
    public AnamneseRequest(Anamnese anamnese) {
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
