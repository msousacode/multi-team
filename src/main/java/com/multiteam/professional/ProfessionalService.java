package com.multiteam.professional;

import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.clinic.ClinicService;
import com.multiteam.core.service.EmailService;
import com.multiteam.treatment.TreatmentService;
import com.multiteam.user.UserRepository;
import com.multiteam.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.*;

import static com.multiteam.core.enums.AuthProviderEnum.local;

@Service
public class ProfessionalService {

    private final Logger logger = LogManager.getLogger(ProfessionalService.class);

    private final ProfessionalRepository professionalRepository;
    private final ClinicService clinicService;
    private final TreatmentService treatmentService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;

    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            ClinicService clinicService,
            TreatmentService treatmentService,
            UserRepository userRepository,
            EmailService emailService,
            UserService userService) {
        this.professionalRepository = professionalRepository;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional
    public Boolean createProfessional(ProfessionalDTO professionalRequest) {

        Set<UUID> clinicsIds = getUuids(professionalRequest);
        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");
        var clinics = clinicService.getClinics(clinicsIds);

        if (clinics.isEmpty()) {
            logger.debug("check if clinic exists. clinicId: {}", professionalRequest.clinicId());
            logger.error("clinic cannot be null. clinicId: {}", professionalRequest.clinicId());
            return Boolean.FALSE;
        }

        var user = userService.createUser(professionalRequest.name(), professionalRequest.email(), local);

        var builder = new Professional.Builder(
                null,
                professionalRequest.name(),
                SpecialtyEnum.get(professionalRequest.specialty()),
                professionalRequest.cellPhone(),
                professionalRequest.email(),
                true,
                clinics,
                user)
                .build();

        professionalRepository.save(builder);

        if(emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())){
            logger.warn("user was created , but an error occurred when sending the first login email: {}", professionalRequest.email());
        }

        return Boolean.TRUE;
    }

    public List<ProfessionalDTO> getAllProfessionals(UUID clinicId) {
        return professionalRepository.findAllProfessionalsByClinicId(clinicId).stream().map(ProfessionalDTO::fromProfessionalDTO).toList();
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

        professionalRepository.professionalInactive(professional.get().getId());
        logger.info("professional has been inactivated, professionalId {}", professionalId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateProfessional(ProfessionalDTO professionalRequest) {

        var professionalResult = professionalRepository.findById(professionalRequest.id());

        Set<UUID> clinicsIds = getUuids(professionalRequest);
        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");
        var clinics = clinicService.getClinics(clinicsIds);

        if (professionalResult.isEmpty()) {
            logger.debug("check if professional exists. professionalId: {}", professionalRequest.id());
            logger.error("professional cannot be null. professionalId: {}", professionalRequest.id());
            return Boolean.FALSE;
        }

        var builder = new Professional.Builder(
                professionalResult.get().getId(),
                professionalRequest.name(),
                SpecialtyEnum.get(professionalRequest.specialty()),
                professionalRequest.cellPhone(),
                professionalRequest.email(),
                true,
                clinics,
                professionalResult.get().getUser())
                .build();

        professionalRepository.save(builder);

        logger.info("updated professional: {}", new Professional().toString());

        return Boolean.TRUE;
    }

    private static Set<UUID> getUuids(ProfessionalDTO professionalRequest) {
        Set<UUID> clinicsIds = new HashSet<>();
        professionalRequest.clinicId().forEach(elem -> clinicsIds.add(UUID.fromString(elem)));
        return clinicsIds;
    }
}
