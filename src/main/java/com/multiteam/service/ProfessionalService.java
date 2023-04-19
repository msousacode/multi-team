package com.multiteam.service;

import com.multiteam.controller.dto.request.ProfessionalRequest;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.multiteam.enums.AuthProviderType.local;

@Service
public class ProfessionalService {

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

        var clinic = clinicService.getClinicById(professionalRequest.clinicId());

        if(clinic.isEmpty()){
            return Boolean.FALSE;
        }

        var user = new User.Builder(null, professionalRequest.name(), professionalRequest.email(), true).provider(local).build();
        var userResult = userRepository.save(user);

        var builder = new Professional.Builder(
                null,
                professionalRequest.name(),
                professionalRequest.middleName(),
                professionalRequest.specialty(),
                professionalRequest.cellPhone(),
                professionalRequest.email(),
                true,
                clinic.get(),
                userResult)
                .build();

        professionalRepository.save(builder);

        return Boolean.TRUE;
    }

    public List<Professional> getAllProfessionals(final UUID clinicId) {
        return professionalRepository.findAllByClinic_Id(clinicId);
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
                result.get().getClinic(),
                professional.getUser())
                .build();

        professionalRepository.save(builder);

        return Boolean.TRUE;
    }
}
