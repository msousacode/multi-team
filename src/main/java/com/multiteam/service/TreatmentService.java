package com.multiteam.service;

import com.multiteam.controller.dto.request.TreatmentRequest;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.entity.TreatmentProfessional;
import com.multiteam.persistence.projection.TreatmentView;
import com.multiteam.persistence.repository.TreatementProfessionalRepository;
import com.multiteam.persistence.repository.TreatmentRepository;
import com.multiteam.enums.SituationEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
            @Lazy PatientService patientService,
            @Lazy ProfessionalService professionalService) {
        this.treatmentRepository = treatmentRepository;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
    }

    @Transactional
    public Boolean includeTreatment(TreatmentRequest treatmentRequest) {

        var patient = patientService.getPatient(treatmentRequest.patientId(), treatmentRequest.clinicId());
        var professional = professionalService.getProfessionalById(treatmentRequest.professionalId());

        if (patient.isEmpty())
            return Boolean.FALSE;
        if (professional.isEmpty())
            return Boolean.FALSE;

        var builder = new Treatment.Builder(
                null,
                treatmentRequest.treatmentType(),
                treatmentRequest.situation(),
                treatmentRequest.initialDate(),
                patient.get())
                .finalDate(treatmentRequest.finalDate())
                .description(treatmentRequest.description())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        var treatmentProfessional = new TreatmentProfessional(null, treatment, professional.get(), "", SituationEnum.ANDAMENTO);
        treatementProfessionalRepository.save(treatmentProfessional);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(TreatmentRequest treatmentRequest) {

        var result = treatmentRepository.findById(treatmentRequest.id());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Treatment.Builder(
                result.get().getId(),
                treatmentRequest.treatmentType(),
                treatmentRequest.situation(),
                treatmentRequest.initialDate(),
                result.get().getPatient())
                .finalDate(treatmentRequest.finalDate())
                .build();

        treatmentRepository.save(builder);

        return Boolean.TRUE;
    }

    public Set<Treatment> getAllTreatmentsByPatientId(UUID patientId) {
        return treatmentRepository.findAllByPatient_Id(patientId);
    }

    @Transactional
    public Boolean inactiveTreatment(UUID treatmentId) {

        var treatment = treatmentRepository.findById(treatmentId);

        if (treatment.isEmpty())
            return Boolean.FALSE;

        //inactive professionals of treatment
        treatementProfessionalRepository.inactiveProfessionalsByTreatmentId(treatmentId, SituationEnum.INATIVO);

        //exclude all guests
        treatment.get().getGuests().removeAll(treatment.get().getGuests());
        treatmentRepository.save(treatment.get());

        //inactive treatment
        treatmentRepository.inactiveTreatment(treatmentId);

        return Boolean.TRUE;
    }

    @Transactional
    public void includeGuestInTreatment(Guest guest, Set<Treatment> treatments) {
        treatments.forEach(treatment -> {
            treatment.addGuestsInTreatment(guest);
            treatmentRepository.save(treatment);
        });
    }

    public List<TreatmentView> getAllTreatmentsByGuestId(UUID guestId) {
        return treatmentRepository.getAllTreatmentsByGuestId(guestId);
    }

    @Transactional
    public void excludeTreatmentByPatientId(UUID patientId) {
        var treatments = getAllTreatmentsByPatientId(patientId);
        treatments.forEach(t -> inactiveTreatment(t.getId()));
    }

    public Optional<TreatmentView> getTreatmentById(UUID treatmentId) {
        return treatmentRepository.findByIdProjection(treatmentId);
    }
}
