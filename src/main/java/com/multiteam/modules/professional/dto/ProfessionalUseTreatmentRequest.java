package com.multiteam.modules.professional.dto;

import com.multiteam.modules.clinic.dto.ClinicDTO;

import java.util.List;

public record ProfessionalUseTreatmentRequest(List<ClinicDTO> clinics) {
}
