package com.multiteam.modules.schedule;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.ScheduleEnum;
import com.multiteam.core.exception.ScheduleException;
import com.multiteam.modules.clinic.ClinicService;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  private final Logger logger = LogManager.getLogger(ScheduleService.class);

  private final ProfessionalService professionalService;
  private final ClinicService clinicService;
  private final PatientService patientService;
  private final ScheduleRepository scheduleRepository;
  private final TenantContext tenantContext;

  public ScheduleService(
      final ProfessionalService professionalService,
      final ClinicService clinicService,
      final PatientService patientService,
      final ScheduleRepository scheduleRepository,
      final TenantContext tenantContext) {
    this.professionalService = professionalService;
    this.clinicService = clinicService;
    this.patientService = patientService;
    this.scheduleRepository = scheduleRepository;
    this.tenantContext = tenantContext;
  }

  @Transactional
  public Boolean createSchedule(ScheduleRequest scheduleRequest) {

    checkIfProfessionalScheduleIsBusy(scheduleRequest.professionalId(), scheduleRequest.start(),
        scheduleRequest.end());

    var professional = professionalService.getProfessionalById(scheduleRequest.professionalId());

    var clinic = clinicService.getClinicById(scheduleRequest.clinicId());

    if (professional.isEmpty() || clinic.isEmpty()) {
      logger.error("values professional or clinic cannot be null or empty");
      return Boolean.FALSE;
    }

    var title = professional.get().getSpecialty().getName().concat(" | ")
        .concat(professional.get().getName());

    Patient patient = null;
    if (scheduleRequest.patient() != null) {
      patient = patientService.findOneById(scheduleRequest.patient().id()).orElse(null);
      title = title.concat(" | ")
          .concat(patient.getName())
          .concat(" | Nasc: ")
          .concat(patient.getDateBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    var builder = new Schedule.Builder(
        title,
        scheduleRequest.start(),
        professional.get(),
        clinic.get(),
        true,
        ScheduleEnum.AGENDADO)
        .patient(patient)
        .end(scheduleRequest.end())
        .description(scheduleRequest.description())
        .color(ScheduleEnum.AGENDADO.getColor()).build();

    scheduleRepository.save(builder);

    return Boolean.TRUE;
  }

  public List<ScheduleResponse> getAllById(final UUID clinicId) {
    return scheduleRepository.findAllByClinicIdAndActiveIsTrue(clinicId).stream()
        .map(ScheduleResponse::fromScheduleResponse).toList();
  }

  public Optional<ScheduleResponse> getSchedule(final UUID scheduleId) {
    return scheduleRepository.findOneById(scheduleId).map(ScheduleResponse::fromScheduleResponse);
  }

  @Transactional
  public Boolean updateSchedule(ScheduleRequest scheduleRequest) {

    checkIfProfessionalScheduleIsBusy(scheduleRequest.professionalId(), scheduleRequest.start(),
        scheduleRequest.end());

    var professional = professionalService.getProfessionalById(scheduleRequest.professionalId());

    var clinic = clinicService.getClinicById(scheduleRequest.clinicId());

    if (professional.isEmpty() || clinic.isEmpty()) {
      logger.error(ApplicationError.PROFESSIONAL_OR_CLINIC_NOT_CAN_BE_EMPTY.getMessage());
      return Boolean.FALSE;
    }

    String title = generateTitleSchedule(professional.get());

    Patient patient = null;
    if (scheduleRequest.patient() != null) {
      patient = patientService.findOneById(scheduleRequest.patient().id()).get();
      title = title.concat(patient.getName()).concat(" | Nasc: ")
          .concat(patient.getDateBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    var schedule = scheduleRepository.findOneById(scheduleRequest.id());

    if (schedule.isPresent()) {

      schedule.get().setTitle(title);
      schedule.get().setProfessional(professional.get());
      schedule.get().setStatus(ScheduleEnum.valueOf(scheduleRequest.status()));
      schedule.get().setColor(ScheduleEnum.getColorByStatus(scheduleRequest.status()));
      schedule.get().setStart(scheduleRequest.start());
      schedule.get().setEnd(scheduleRequest.end());
      schedule.get().setDescription(scheduleRequest.description());
      schedule.get().setPatient(patient);

      scheduleRepository.save(schedule.get());

    } else {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  @Transactional
  public void inactiveSchedule(final UUID scheduleId) {
    scheduleRepository.inactiveScheduleById(scheduleId, tenantContext.getTenantId());
  }

  private void validateDates(LocalDateTime start, LocalDateTime end) {

    LocalDate currentDate = LocalDate.now();

    if(start.isBefore(currentDate.atStartOfDay())){
      logger.error(ApplicationError.CONFLICT_CURRENT_DATE.getMessage());
      throw new ScheduleException(ApplicationError.CONFLICT_CURRENT_DATE.getMessage());
    }

     if(end.isBefore(start)){
       logger.error(ApplicationError.CONFLICT_DATES.getMessage());
       throw new ScheduleException(ApplicationError.CONFLICT_DATES.getMessage());
     }
  }

  private void checkIfProfessionalScheduleIsBusy(UUID professionalId, LocalDateTime start,
      LocalDateTime end) {
    validateDates(start, end);
    if (!scheduleRepository.findAllScheduleOfProfessional(professionalId, start, end).isEmpty()) {
      logger.error(ApplicationError.CONFLICT_SCHEDULE.getMessage());
      throw new ScheduleException(ApplicationError.CONFLICT_SCHEDULE.getMessage());
    }
  }

  private String generateTitleSchedule(Professional professional) {
    return professional
        .getSpecialty().getName()
        .concat(" | ")
        .concat(professional.getName())
        .concat(" | ");
  }
}
