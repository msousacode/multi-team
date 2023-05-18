package com.multiteam.modules.professional.dto;

import com.multiteam.modules.clinic.ClinicDTO;
import com.multiteam.modules.professional.Professional;

import java.util.List;
import java.util.UUID;

public record ProfessionalUseTreatmentResponse(
        UUID id,
        String name,
        String specialty,
        String cellPhone,
        String email,
        List<ClinicDTO> clinics
) {
    public static ProfessionalUseTreatmentResponse fromProfessionalDTO(Professional professional) {
        return new ProfessionalUseTreatmentResponse(
                professional.getId(),
                professional.getName(),
                professional.getSpecialty().getName(),
                professional.getCellPhone(),
                professional.getEmail(),
                professional.getClinics().stream().map(ClinicDTO::new).toList()
        );
    }
}
