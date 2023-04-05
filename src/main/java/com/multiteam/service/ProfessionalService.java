package com.multiteam.service;

import com.multiteam.controller.dto.ResponseDto;
import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.service.util.ProvisinalPasswordUtil;
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
    public Professional createProfessional(Professional professional, UUID clinicId) {

        var clinic = clinicService.findById(clinicId);

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

        return professionalRepository.save(builder);
    }

    public List<Professional> getAllProfessionals(final UUID clinicId) {
        return professionalRepository.findAllByClinic_Id(clinicId);
    }

    public Optional<Professional> getProfessionalById(final UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    public ResponseDto getProfessional(UUID professionalId) {
        var professional = professionalRepository.findById(professionalId);
        return professional.isPresent() ? new ResponseDto(professional, "", true) : new ResponseDto(null, "resource not found", false);
    }

    @Transactional
    public ResponseDto inactiveProfessional(UUID professionalId) {

        var professional = professionalRepository.findById(professionalId);

        if (professional.isEmpty()) {
            return new ResponseDto(null, "resource not found", false);
        }

        professional.get().getProfessionals().forEach(t -> treatmentService.inactiveTreatment(t.getTreatment().getId()));
        professionalRepository.professionalInactive(professional.get().getId());

        return new ResponseDto(null, "professional deleted successfully", true);
    }

    @Transactional
    public ResponseDto updateProfessional(Professional professional) {

        var result = professionalRepository.findById(professional.getId());

        if(result.isEmpty()){
            return new ResponseDto(null, "resource not found", false);
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

        return new ResponseDto(null, "successfully updated professional", true);
    }
}
