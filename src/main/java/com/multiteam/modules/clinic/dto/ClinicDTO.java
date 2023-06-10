package com.multiteam.modules.clinic.dto;

import com.multiteam.modules.clinic.Clinic;

import java.util.UUID;

public record ClinicDTO(
        UUID id,
        String clinicName,
        String cpfCnpj,
        String email,
        String cellPhone,
        String telephone,
        String observation,
        String site,
        String inscEstadual,
        String zipCode,
        String address,
        String number,
        String district,
        String city,
        String state
) {
    public ClinicDTO(Clinic clinic) {
        this(
                clinic.getId(),
                clinic.getClinicName(),
                clinic.getCpfCnpj(),
                clinic.getEmail(),
                clinic.getCellPhone(),
                clinic.getTelephone(),
                clinic.getObservation(),
                clinic.getSite(),
                clinic.getInscEstadual(),
                clinic.getZipCode(),
                clinic.getAddress(),
                clinic.getNumber(),
                clinic.getDistrict(),
                clinic.getCity(),
                clinic.getState()
        );
    }

    public static ClinicDTO createWithIdAndName(Clinic clinic) {
        return new ClinicDTO(clinic.getId(), clinic.getClinicName(), null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
