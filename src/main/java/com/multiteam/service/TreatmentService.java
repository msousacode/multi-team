package com.multiteam.service;

import com.multiteam.controller.dto.TreatmentDto;
import com.multiteam.vo.DataResponse;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.entity.TreatmentProfessional;
import com.multiteam.persistence.repository.TreatementProfessionalRepository;
import com.multiteam.persistence.repository.TreatmentRepository;
import com.multiteam.persistence.types.SituationType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatementProfessionalRepository treatementProfessionalRepository;
    private final PatientService patientService;
    private final ProfessionalService professionalService;

    public TreatmentService(
            TreatmentRepository treatmentRepository,
            TreatementProfessionalRepository treatementProfessionalRepository,
            PatientService patientService,
            ProfessionalService professionalService) {
        this.treatmentRepository = treatmentRepository;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
    }

    @Transactional
    public DataResponse includeTreatment(TreatmentDto treatmentDto) {

        var patient = patientService.getPatientById(treatmentDto.patientId(), treatmentDto.clinicId());
        var professional = professionalService.getProfessionalById(treatmentDto.professionalId());

        if (patient.isEmpty())
            return new DataResponse(null, "patient not found, try again", false);
        if(professional.isEmpty())
            return new DataResponse(null, "professionals not found, try again", false);

        var builder = new Treatment.Builder(
                null,
                treatmentDto.treatmentType(),
                treatmentDto.situation(),
                treatmentDto.initialDate(),
                patient.get())
                .description(treatmentDto.description())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        var treatmentProfessional = new TreatmentProfessional(null, treatment, professional.get(), "", SituationType.ANDAMENTO);
        treatementProfessionalRepository.save(treatmentProfessional);

        return new DataResponse(null, "treatment added with success", true);
    }

    public Set<Treatment> getAllTreatmentsByPatientId(UUID patientId) {
        return treatmentRepository.findAllByPatient_Id(patientId);
    }

    @Transactional
    public DataResponse excludeTreatment(UUID treatmentId) {

        var treatment = treatmentRepository.findById(treatmentId);

        if(treatment.isEmpty())
            return new DataResponse(null, "treatment not found try again", false);

        treatementProfessionalRepository.deleteByTreatment_Id(treatmentId);
        treatmentRepository.delete(treatment.get());

        return new DataResponse(null, "treatment deleted with success", true);
    }

    @Transactional
    public void includeGuestInTreatment(Guest guest, Set<Treatment> treatments) {
        treatments.forEach(treatment -> {
            treatment.addGuestsInTreatment(guest);
            treatmentRepository.save(treatment);
        });
    }

    public List<Treatment> getAllTreatmentsByGuestId(UUID guestId) {
        return treatmentRepository.getAllTreatmentsByGuestId(guestId);
    }
}
