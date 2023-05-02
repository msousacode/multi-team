package com.multiteam.service;

import com.multiteam.controller.dto.ProfessionalDTO;
import com.multiteam.enums.SpecialtyEnum;
import com.multiteam.exception.OwnerException;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.*;

import static com.multiteam.enums.AuthProviderEnum.local;

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

        if(professionalRequest.ownerId() == null) {
            throw new OwnerException("value ownerId cannot be null");
        }

        if (clinics.isEmpty()) {
            logger.debug("check if clinic exists. clinicId: {}", professionalRequest.clinicId());
            logger.error("clinic cannot be null. clinicId: {}", professionalRequest.clinicId());
            return Boolean.FALSE;
        }

        var user = userService.createUser(professionalRequest.name(), professionalRequest.email(), professionalRequest.ownerId(), local);

        var userResult = userRepository.save(user);

        var builder = new Professional.Builder(
                null,
                professionalRequest.name(),
                SpecialtyEnum.get(professionalRequest.specialty()),
                professionalRequest.cellPhone(),
                professionalRequest.email(),
                true,
                clinics,
                userResult,
                professionalRequest.ownerId())
                .build();

        professionalRepository.save(builder);

        if(emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())){
            logger.warn("user was created , but an error occurred when sending the first login email: {}", professionalRequest.email());
        }

        return Boolean.TRUE;
    }

    public List<Professional> getAllProfessionals(final UUID clinicId) {
        return professionalRepository.findAllProfessionalsByClinicId(clinicId);
    }

    public Optional<Professional> getProfessionalById(final UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    public Optional<ProfessionalDTO> getProfessional(UUID professionalId) {
        var professional = professionalRepository.findById(professionalId);

        if (professional.isEmpty()) {
            return Optional.empty();
        }

        Set<String> clinicsIds = new HashSet<>();
        professional.get().getClinics().forEach(i -> clinicsIds.add(i.getId().toString()));

        return professional.map(i -> new ProfessionalDTO(
                i.getId(), i.getName(), i.getSpecialty().getName(), i.getCellPhone(), i.getEmail(), clinicsIds, i.getOwnerId()));
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
                professionalResult.get().getUser(),
                professionalResult.get().getOwnerId())
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
