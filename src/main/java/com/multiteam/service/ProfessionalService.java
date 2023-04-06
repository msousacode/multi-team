package com.multiteam.service;

import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.util.ProvisinalPasswordUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final CredentialService credentialService;
    private final ClinicService clinicService;
    private final TreatmentService treatmentService;

    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            CredentialService credentialService,
            ClinicService clinicService,
            TreatmentService treatmentService) {
        this.professionalRepository = professionalRepository;
        this.credentialService = credentialService;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
    }

    @Transactional
    public Boolean createProfessional(Professional professional, UUID clinicId) {

        var clinic = clinicService.getClinicById(clinicId);

        if(clinic.isEmpty()){
            return Boolean.FALSE;
        }

        var credential = credentialService.createCredential(new Credential(professional.getEmail(), ProvisinalPasswordUtil.generate()));

        var builder = new Professional.Builder(
                null,
                professional.getName(),
                professional.getMiddleName(),
                professional.getSpecialty(),
                professional.getCellPhone(),
                professional.getEmail(),
                professional.isActive(),
                clinic.get())
                .credential(credential)
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
                result.get().getClinic())
                .build();

        professionalRepository.save(builder);

        return Boolean.TRUE;
    }
}
