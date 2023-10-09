package com.multiteam.core.enums;

public enum ApplicationError {

  CONFLICT_SCHEDULE("Conflito de agenda! O profissional já possui agendamento para esse dia e horário. Revise o agendamento ou selecione outro dia e horário"),
  CONFLICT_DATES("Data final deve ser maior que a data inicial"),
  CONFLICT_CURRENT_DATE("Não é possível agendar para uma data menor do que a data atual. Revise o agendamento."),
  PATIENT_NOT_FOUND("Patient not found"),
  PROFESSIONAL_OR_CLINIC_NOT_CAN_BE_EMPTY("Professional or Clinic not can be empty"),
  S3_EXCEPTION("An exception occured while uploading the file"),
  RESOURCE_NOT_FOUND("Não foi possível localizar o recurso uuid: %s");

  final String message;

  ApplicationError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
