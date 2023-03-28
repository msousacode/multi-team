package com.multiteam.service;

import com.multiteam.controller.dto.TreatmentDto;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.entity.TreatmentProfessional;
import com.multiteam.persistence.repository.TreatementProfessionalRepository;
import com.multiteam.persistence.repository.TreatementRepository;
import com.multiteam.persistence.types.SituationType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TreatamentService {

    private final TreatementRepository treatementRepository;
    private final TreatementProfessionalRepository treatementProfessionalRepository;
    private final PatientService patientService;
    private final ProfessionalService professionalService;

    public TreatamentService(
            TreatementRepository treatementRepository,
            TreatementProfessionalRepository treatementProfessionalRepository,
            PatientService patientService,
            ProfessionalService professionalService) {
        this.treatementRepository = treatementRepository;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
    }

    @Transactional
    public Boolean includeTreatment(TreatmentDto treatmentDto) {

        var patient = patientService.getPatientById(treatmentDto.patientId(), treatmentDto.clinicId());
        var professional = professionalService.getProfessionalById(treatmentDto.professionalId());

        if (patient.isEmpty() || professional.isEmpty())
            throw new RuntimeException("");//TODO Exceptions custom in case error.

        var builder = new Treatment.Builder(
                null,
                treatmentDto.treatmentType(),
                treatmentDto.situation(),
                treatmentDto.initialDate(),
                patient.get())
                .build();

        var treatment = treatementRepository.save(builder);

        var treatmentProfessional = new TreatmentProfessional(null, treatment, professional.get(), "", SituationType.ANDAMENTO);
        treatementProfessionalRepository.save(treatmentProfessional);

        return Boolean.TRUE;
    }
}
