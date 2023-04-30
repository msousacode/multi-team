package com.multiteam.controller.dto.request;

import com.multiteam.persistence.entity.Patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientDTO(
        UUID id,
        String name,
        String email,
        String cellPhone,
        String sex,
        Integer age,
        boolean active,
        UUID ownerId,
        LocalDate dateBirth
) {
    private PatientDTO(Patient patient) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getUser().getEmail(),
                patient.getCellPhone(),
                patient.getSex().name(),
                patient.getAge(),
                patient.isActive(),
                patient.getOwnerId(),
                patient.getDateBirth()
        );
    }

    public static PatientDTO fromPatientDTO(Patient patient) {
        return new PatientDTO(patient);
    }
}
