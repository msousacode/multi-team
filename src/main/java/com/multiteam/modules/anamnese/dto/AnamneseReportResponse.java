package com.multiteam.modules.anamnese.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.anamnese.Anamnese;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.user.UserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AnamneseReportResponse(
        String patientName,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dateBirth,
        String professionalName,
        @JsonFormat(pattern = "dd/MM/yyyy : HH:mm")
        LocalDateTime createdDate,
        UUID anamneseId,
        String annotation,
        String conclusion
) {
    private AnamneseReportResponse(Anamnese anamnese, Patient patient, UserDTO user) {
        this(
                patient.getName(),
                patient.getDateBirth(),
                user.name(),
                anamnese.getCreatedDate(),
                anamnese.getId(),
                anamnese.getAnnotation(),
                anamnese.getConclusion()
        );
    }

    public static AnamneseReportResponse fromAnamneseReportResponse(Anamnese anamnese, Patient patient, UserDTO user) {
        return new AnamneseReportResponse(anamnese, patient, user);
    }
}
