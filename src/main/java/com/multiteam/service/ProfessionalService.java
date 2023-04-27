package com.multiteam.service;

import com.multiteam.controller.dto.request.ProfessionalRequest;
import com.multiteam.enums.SpecialtyEnum;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.entity.User;
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

    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            ClinicService clinicService,
            TreatmentService treatmentService,
            UserRepository userRepository) {
        this.professionalRepository = professionalRepository;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createProfessional(ProfessionalRequest professionalRequest) {

        Set<UUID> clinicsIds = new HashSet<>();
        professionalRequest.clinicId().forEach(elem -> clinicsIds.add(UUID.fromString(elem)));

        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");

        var clinics = clinicService.getClinics(clinicsIds);

        if(clinics.isEmpty()){
            logger.debug("check if clinic exists. clinicId: {}", professionalRequest.clinicId());
            logger.error("clinic cannot be null. clinicId: {}", professionalRequest.clinicId());
            return Boolean.FALSE;
        }

        var user = new User.Builder(null, professionalRequest.name(), professionalRequest.email(), true).provider(local).build();
        var userResult = userRepository.save(user);

        var builder = new Professional.Builder(
                null,
                professionalRequest.name(),
                professionalRequest.middleName(),
                SpecialtyEnum.get(professionalRequest.specialty()),
                professionalRequest.cellPhone(),
                professionalRequest.email(),
                true,
                clinics,
                userResult)
                .build();

        professionalRepository.save(builder);

        return Boolean.TRUE;
    }

    public List<Professional> getAllProfessionals(final UUID clinicId) {
        return professionalRepository.findAllByClinics_Id(clinicId);
    }

    public Optional<Professional> getProfessionalById(final UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    public Optional<Professional> getProfessional(UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    @Transactional
    public Boolean inactiveProfessional(UUID professionalId) {

        var professional = professionalRepository.findById(professionalId);

        if (professional.isEmpty()) {
            return Boolean.FALSE;
        }

        professional.get().getProfessionals().forEach(t -> treatmentService.inactiveTreatment(t.getTreatment().getId()));
        professionalRepository.professionalInactive(professional.get().getId());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateProfessional(Professional professional) {

        var result = professionalRepository.findById(professional.getId());

        if(result.isEmpty()){
            return Boolean.FALSE;
        }

        var builder = new Professional.Builder(
                result.get().getId(),
                professional.getName(),
                professional.getMiddleName(),
                professional.getSpecialty(),
                professional.getCellPhone(),
                professional.getEmail(),
                professional.isActive(),
                List.of(),
                professional.getUser())
                .build();

        professionalRepository.save(builder);

        return Boolean.TRUE;
    }
}
