package com.multiteam.treatment;

import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.patient.Patient;
import com.multiteam.patient.PatientService;
import com.multiteam.guest.Guest;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.professional.Professional;
import com.multiteam.professional.ProfessionalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TreatmentService {

    private static final Logger logger = LogManager.getLogger(TreatmentService.class);

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
    public Boolean includeTreatment(TreatmentDTO treatmentDTO) {
        logger.info("include treatment to patient id {}", treatmentDTO.patientId());
        var patient = getPatient(treatmentDTO);
        var professional = getProfessional(treatmentDTO);

        if (patient.isEmpty() || professional.isEmpty()) {
            logger.error("patient or professional cannot be empty");
            throw new BadRequestException("patient or professional cannot be empty");
        }

        var builder = new Treatment.Builder(
                null,
                TreatmentEnum.get(professional.get().getSpecialty().getName()),
                treatmentDTO.situation(),
                treatmentDTO.initialDate(),
                patient.get())
                .finalDate(treatmentDTO.finalDate())
                .description(treatmentDTO.description())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        var treatmentProfessional = new TreatmentProfessional(null, treatment, professional.get(), "", SituationEnum.ANDAMENTO);
        treatementProfessionalRepository.save(treatmentProfessional);

        logger.info("successfully included treatment id: {}", treatment.getId());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(TreatmentDTO treatmentDTO) {

        var result = treatmentRepository.findById(treatmentDTO.id());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        var patient = getPatient(treatmentDTO);
        var professional = getProfessional(treatmentDTO);

        if (patient.isEmpty() || professional.isEmpty()) {
            logger.error("patient or professional cannot be empty");
            throw new BadRequestException("patient or professional cannot be empty");
        }

        var builder = new Treatment.Builder(
                treatmentDTO.id(),
                TreatmentEnum.get(professional.get().getSpecialty().getName()),
                treatmentDTO.situation(),
                treatmentDTO.initialDate(),
                result.get().getPatient())
                .description(treatmentDTO.description())
                .finalDate(treatmentDTO.finalDate())
                .build();

        treatmentRepository.save(builder);
        logger.info("successfully updated treatment");
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

    private Optional<Patient> getPatient(TreatmentDTO treatmentDTO) {
        var patient = patientService.getPatient(treatmentDTO.patientId(), treatmentDTO.clinicId());
        if (patient.isEmpty()) {
            logger.error("patient not found. It is necessary to have a patient to include the treatment");
            return Optional.empty();
        }
        return patient;
    }

    private Optional<Professional> getProfessional(TreatmentDTO treatmentDTO) {
        var professional = professionalService.getProfessionalById(treatmentDTO.professionalId());
        if (professional.isEmpty()) {
            logger.error("professional not found. It is necessary to have a professional to include the treatment");
            return Optional.empty();
        }
        return professional;
    }
}
