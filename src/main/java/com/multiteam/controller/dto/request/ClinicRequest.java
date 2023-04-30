package com.multiteam.controller.dto.request;

import java.util.UUID;

public record ClinicRequest(
        UUID id,
        String clinicName,
        String cpfCnpj,
        String email,
        String cellPhone,
        String telephone,
        String observation,
        UUID userId
) {
}
