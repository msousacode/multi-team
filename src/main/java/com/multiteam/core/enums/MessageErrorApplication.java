package com.multiteam.core.enums;

public enum MessageErrorApplication {

  CONFLICT_SCHEDULE("Conflito de agenda! O profissional já possui agendamento para esse dia e horário. Revise o agendamento ou selecione outro dia e horário"),
  CONFLICT_DATES("Data final deve ser maior que a data inicial"),
  CONFLICT_CURRENT_DATE("Não é possível agendar para uma data menor do que a data atual. Revise o agendamento."),
  PROFESSIONAL_OR_CLINIC_NOT_CAN_BE_EMPTY("Professional or Clinic not can be empty");

  final String message;

  MessageErrorApplication(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}