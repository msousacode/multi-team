package com.multiteam.service;

import com.multiteam.controller.dto.request.TreatmentRequest;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.entity.TreatmentProfessional;
import com.multiteam.persistence.projection.TreatmentView;
import com.multiteam.persistence.repository.TreatementProfessionalRepository;
import com.multiteam.persistence.repository.TreatmentRepository;
import com.multiteam.enums.SituationType;
import org.springframework.context.annotation.Lazy;
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
            @Lazy PatientService patientService,
            @Lazy ProfessionalService professionalService) {
        this.treatmentRepository = treatmentRepository;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
    }

    @Transactional
    public Boolean includeTreatment(TreatmentRequest treatmentDto) {

        var patient = patientService.getPatientById(treatmentDto.patientId(), treatmentDto.clinicId());
        var professional = professionalService.getProfessionalById(treatmentDto.professionalId());

        if (patient.isEmpty())
            return Boolean.FALSE;
        if (professional.isEmpty())
            return Boolean.FALSE;

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

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(Treatment treatment) {

        var result = treatmentRepository.findById(treatment.getId());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Treatment.Builder(
                result.get().getId(),
                treatment.getTreatmentType(),
                treatment.getSituation(),
                treatment.getInitialDate(),
                result.get().getPatient())
                .finalDate(treatment.getFinalDate())
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
        treatementProfessionalRepository.inactiveProfessionalsByTreatmentId(treatmentId, SituationType.INATIVO);

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

    public TreatmentView getTreatmentById(UUID treatmentId) {
        return treatmentRepository.findByIdProjection(treatmentId);
    }
}
