package com.multiteam.modules.patient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.treatment.Treatment;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    String cpf,
    String treatment
) {
  
  static UUID treatmentId;

  private PatientDTO(Patient patient) {
    this(
        patient.getId(),
        patient.getName(),
        patient.getUser().getEmail(),
        patient.getCellPhone(),
        patient.getSex().getDescription(),
        patient.getAge(),
        patient.getDateBirth(),
        patient.getCpf(),
        buildTreatment(patient.getTreatments())
    );
  }

  public static PatientDTO fromPatientDTO(Patient patient) {
    return new PatientDTO(patient);
  }

  private static String buildTreatment(List<Treatment> treatments) {

    if (!treatments.isEmpty()) {

      var treatment = treatments.get(0);

      treatmentId = treatment.getId();

      return """
          Paciente possui tratamento em %s com data ínicio %s até %s""".formatted(
          treatment.getSituation(),
          treatment.getInitialDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
          treatment.getFinalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    return "";
  }
}
