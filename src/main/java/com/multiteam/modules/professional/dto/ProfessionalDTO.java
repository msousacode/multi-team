package com.multiteam.modules.professional.dto;

import com.multiteam.modules.professional.Professional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record ProfessionalDTO(
        UUID id,
        String name,
        String specialty,
        String cellPhone,
        String email,
        Set<String> clinicId
) {
    public static ProfessionalDTO fromProfessionalDTO(Professional professional) {
        Set<String> clinicsIds = new HashSet<>();
        professional.getClinics().forEach(i -> clinicsIds.add(i.getId().toString()));
        return new ProfessionalDTO(
                professional.getId(),
                professional.getName(),
                professional.getSpecialty().getName(),
                professional.getCellPhone(),
                professional.getEmail(),
                clinicsIds);
    }
}
