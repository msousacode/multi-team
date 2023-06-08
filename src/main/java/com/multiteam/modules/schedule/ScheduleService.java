package com.multiteam.modules.schedule;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.ScheduleEnum;
import com.multiteam.modules.clinic.ClinicService;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.ProfessionalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        var professional = professionalService.getProfessionalById(scheduleRequest.professionalId());

        var clinic = clinicService.getClinicById(scheduleRequest.clinicId());


        if (professional.isEmpty() || clinic.isEmpty()) {
            logger.error("values professional or clinic cannot be null or empty");
            return Boolean.FALSE;
        }

        var title = professional.get().getSpecialty().getName().concat(" | ").concat(professional.get().getName());

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
                ScheduleEnum.CONFIRMADO)
                .patient(patient)
                .end(scheduleRequest.end())
                .description(scheduleRequest.description())
                .color("green").build();

        scheduleRepository.save(builder);

        return Boolean.TRUE;
    }

    public List<ScheduleResponse> getAllById(final UUID clinicId) {
        return scheduleRepository.findAllByClinicIdAndActiveIsTrue(clinicId).stream().map(ScheduleResponse::fromScheduleResponse).toList();
    }

    public Optional<ScheduleResponse> getSchedule(final UUID scheduleId) {
        return scheduleRepository.findOneById(scheduleId).map(ScheduleResponse::fromScheduleResponse);
    }

    @Transactional
    public boolean updateSchedule(ScheduleRequest scheduleRequest) {

        var professional = professionalService.getProfessionalById(scheduleRequest.professionalId());

        var clinic = clinicService.getClinicById(scheduleRequest.clinicId());

        if (professional.isEmpty() || clinic.isEmpty()) {
            return Boolean.FALSE;
        }

        var title = professional.get()
                .getSpecialty().getName()
                .concat(" | ")
                .concat(professional.get().getName())
                .concat(" | ");

        Patient patient = null;
        if (scheduleRequest.patient() != null) {
            patient = patientService.findOneById(scheduleRequest.patient().id()).get();
            title = title.concat(patient.getName())
                    .concat(" | Nasc: ")
                    .concat(patient.getDateBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        var schedule = scheduleRepository.findOneById(scheduleRequest.id());

        if (schedule.isPresent()) {
            var builder = new Schedule.Builder(
                    title,
                    scheduleRequest.start(),
                    professional.get(),
                    clinic.get(),
                    true,
                    ScheduleEnum.valueOf(scheduleRequest.status()))
                    .color(ScheduleEnum.get(scheduleRequest.status()))
                    .end(scheduleRequest.end())
                    .description(scheduleRequest.description())
                    .patient(patient).id(schedule.get().getId())
                    .build();

            scheduleRepository.save(builder);

        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Transactional
    public void inactiveSchedule(final UUID scheduleId) {
        scheduleRepository.inactiveScheduleById(scheduleId, tenantContext.getTenantId());
    }
}
