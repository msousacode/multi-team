package com.multiteam.professional;

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
    public static ProfessionalDTO fromProfessionalDTO(Professional p) {
        Set<String> clinicsIds = new HashSet<>();
        p.getClinics().forEach(i -> clinicsIds.add(i.getId().toString()));
        return new ProfessionalDTO(
                p.getId(),
                p.getName(),
                p.getSpecialty().getName(),
                p.getCellPhone(),
                p.getEmail(),
                clinicsIds);
    }
}
