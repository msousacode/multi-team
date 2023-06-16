package com.multiteam.modules.patient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.patient.Patient;
import java.time.LocalDate;
import java.util.UUID;

public record PatientDTO(
    UUID id,
    String name,
    String email,
    String cellPhone,
    String sex,
    Integer age,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateBirth,
    String cpf
) {

  private PatientDTO(Patient patient) {
    this(
        patient.getId(),
        patient.getName(),
        patient.getUser().getEmail(),
        patient.getCellPhone(),
        patient.getSex().getDescription(),
        patient.getAge(),
        patient.getDateBirth(),
        patient.getCpf()
    );
  }

  public static PatientDTO fromPatientDTO(Patient patient) {
    return new PatientDTO(patient);
  }
}
