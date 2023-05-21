package com.multiteam.modules.schedule;

import com.multiteam.core.enums.ScheduleEnum;
import com.multiteam.modules.clinic.ClinicService;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.ProfessionalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ScheduleService {

    private final Logger logger = LogManager.getLogger(ScheduleService.class);

    private final ProfessionalService professionalService;
    private final ClinicService clinicService;
    private final PatientService patientService;
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(
            final ProfessionalService professionalService,
            final ClinicService clinicService,
            final PatientService patientService,
            final ScheduleRepository scheduleRepository) {
        this.professionalService = professionalService;
        this.clinicService = clinicService;
        this.patientService = patientService;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public Boolean createSchedule(ScheduleRequest scheduleRequest) {

        var professional = professionalService.getProfessionalById(scheduleRequest.professionalId());

        var clinic = clinicService.getClinicById(scheduleRequest.clientId());


        if (professional.isEmpty() || clinic.isEmpty()) {
            logger.error("values professional or clinic cannot be null or empty");
            return Boolean.FALSE;
        }

        Patient patient = null;
        if (scheduleRequest.patientId() != null) {
            patient = patientService.findOneById(scheduleRequest.patientId()).orElse(null);
        }

        var builder = new Schedule.Builder(
                scheduleRequest.title(),
                scheduleRequest.start(),
                professional.get(),
                clinic.get(),
                true,
                ScheduleEnum.CONFIRMADO)
                .patient(patient)
                .end(scheduleRequest.end())
                .url(scheduleRequest.url())
                .description(scheduleRequest.description())
                .color("green").build();

        scheduleRepository.save(builder);

        return Boolean.TRUE;
    }

/*
    public Page<ProfessionalDTO> getAllProfessionals(final UUID clinicId, Pageable pageable) {
        return professionalRepository.findAllProfessionalsByClinicId(clinicId, pageable).map(ProfessionalDTO::fromProfessionalDTO);
    }

    public Optional<Professional> getProfessionalById(final UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    public Optional<ProfessionalDTO> getProfessional(UUID professionalId) {
        var professional = professionalRepository.findOneById(professionalId);

        if (professional.isEmpty()) {
            return Optional.empty();
        }

        return professional.map(ProfessionalDTO::fromProfessionalDTO);
    }

    @Transactional
    public Boolean inactiveProfessional(UUID professionalId) {

        var professional = professionalRepository.findById(professionalId);

        if (professional.isEmpty()) {
            logger.error("verify if exists professional with id: {}", professionalId);
            return Boolean.FALSE;
        }

        professional.get().getProfessionals().forEach(t -> treatmentService.inactiveTreatment(t.getTreatment().getId()));
        logger.info("treatments associated with professional {} has been inactivated", professionalId);

        professionalRepository.professionalInactive(professional.get().getId(), tenantContext.getTenantId());
        logger.info("professional has been inactivated, professionalId {}", professionalId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateProfessional(ProfessionalDTO professionalDTO) {

        var professionalResult = professionalRepository.findById(professionalDTO.id());

        Set<UUID> clinicsIds = getUuids(professionalDTO);
        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");
        var clinics = clinicService.getClinics(clinicsIds);

        if (professionalResult.isEmpty()) {
            logger.debug("check if professional exists. professionalId: {}", professionalDTO.id());
            logger.error("professional cannot be null. professionalId: {}", professionalDTO.id());
            return Boolean.FALSE;
        }

        var builder = new Professional.Builder(
                professionalResult.get().getId(),
                professionalDTO.name(),
                SpecialtyEnum.get(professionalDTO.specialty()),
                professionalDTO.cellPhone(),
                professionalDTO.email(),
                true,
                clinics,
                professionalResult.get().getUser())
                .build();

        professionalRepository.save(builder);

        logger.info("updated professional: {}", new Professional().toString());

        return Boolean.TRUE;
    }

    private static Set<UUID> getUuids(ProfessionalDTO professionalDTO) {
        Set<UUID> clinicsIds = new HashSet<>();
        professionalDTO.clinicId().forEach(elem -> clinicsIds.add(UUID.fromString(elem)));
        return clinicsIds;
    }

    public List<ProfessionalUseTreatmentView> getProfessionalsUseTreatment(ProfessionalUseTreatmentRequest professionalUseTreatmentDTO) {
        List<UUID> ids = new ArrayList<>();
        professionalUseTreatmentDTO.clinics().forEach(clinicDTO -> ids.add(clinicDTO.id()));
        return professionalRepository.findAllByClinicsId(ids);
    }

    public List<Professional> getAllProfessionalsByClinics(List<UUID> professionals) {
        return professionalRepository.getAllProfessionalsByClinics(professionals);
    }
 */
}
