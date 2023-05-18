package com.multiteam.modules.professional.dto;

import com.multiteam.modules.clinic.ClinicDTO;

import java.util.List;

public record ProfessionalUseTreatmentRequest(List<ClinicDTO> clinics) {
}
