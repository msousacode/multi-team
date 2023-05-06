package com.multiteam.clinic.dto;

import java.util.UUID;

public record ClinicDTO(
        UUID id,
        String clinicName,
        String cpfCnpj,
        String email,
        String cellPhone,
        String telephone,
        String observation
) {
}
