package com.multiteam.service;

import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.repository.ProfessionalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final CredentialService credentialService;
    private final ClinicService clinicService;

    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            CredentialService credentialService,
            ClinicService clinicService) {
        this.professionalRepository = professionalRepository;
        this.credentialService = credentialService;
        this.clinicService = clinicService;
    }

    @Transactional
    public Professional createProfessional(Professional professional, UUID clinicId) {

        var clinic = clinicService.findById(clinicId);

        var credential = credentialService.createCredential(new Credential(professional.getEmail(), "12345678"));//TODO ajustar para não gerar essa senha.

        var builder = new Professional.Builder(
                null,
                professional.getName(),
                professional.getMiddleName(),
                professional.getSpecialty(),
                professional.getCellPhone(),
                professional.getEmail(),
                professional.isActive(),
                clinic)
                .credential(credential)
                .build();

        return professionalRepository.save(builder);
    }

    public List<Professional> getProfessionals() {
        return professionalRepository.findAll();
    }
}
