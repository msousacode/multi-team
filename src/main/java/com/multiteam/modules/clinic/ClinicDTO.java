package com.multiteam.modules.clinic;

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
    public ClinicDTO(Clinic clinic) {
        this(
                clinic.getId(),
                clinic.getClinicName(),
                clinic.getCpfCnpj(),
                clinic.getEmail(),
                clinic.getCellPhone(),
                clinic.getTelephone(),
                clinic.getObservation()
        );
    }
}
