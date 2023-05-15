package com.multiteam.treatment;

import com.multiteam.core.context.TenantContext;
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
import java.time.LocalDate;
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
    private final TenantContext tenantContext;

    public TreatmentService(
            TreatmentRepository treatmentRepository,
            TreatementProfessionalRepository treatementProfessionalRepository,
            @Lazy PatientService patientService,
            @Lazy ProfessionalService professionalService,
            TenantContext tenantContext) {
        this.treatmentRepository = treatmentRepository;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
        this.tenantContext = tenantContext;
    }

    @Transactional
    public Boolean createTreatment(TreatmentDTO treatmentDTO) {
        logger.info("include treatment to patient id {}", treatmentDTO.patientId());
        var patient = getPatient(treatmentDTO);
        var professionals = getProfessional(treatmentDTO);//TODO Buscar profissionais

        if (patient.isEmpty() || professionals.isEmpty()) {
            logger.error("patient or professional cannot be empty");
            throw new BadRequestException("patient or professional cannot be empty");
        }

        var builder = new Treatment.Builder(
                null,
                TreatmentEnum.FONOAUDIOLOGIA,
                SituationEnum.ANDAMENTO,
                treatmentDTO.initialDate(),
                patient.get())
                .finalDate(treatmentDTO.finalDate())
                .description(treatmentDTO.observation())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        professionals.forEach(professional -> {
            var treatmentProfessional = new TreatmentProfessional(null, treatment, professional, "", SituationEnum.ANDAMENTO);
            treatementProfessionalRepository.save(treatmentProfessional);
        });

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
            throw new BadRequestException("patient or professional cannot be empty or null");
        }

        var builder = new Treatment.Builder(
                treatmentDTO.id(),
                TreatmentEnum.FONOAUDIOLOGIA,
                SituationEnum.ANDAMENTO,
                LocalDate.now(),
                result.get().getPatient())
                .description(treatmentDTO.observation())
                .finalDate(LocalDate.now())
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

    public List<TreatmentResponse> getAllTreatmentsByGuestId(UUID guestId) {
        //return treatmentRepository.getAllTreatmentsByGuestId(guestId);
        return List.of();
    }

    @Transactional
    public void excludeTreatmentByPatientId(UUID patientId) {
        var treatments = getAllTreatmentsByPatientId(patientId);
        treatments.forEach(t -> inactiveTreatment(t.getId()));
    }

    public Optional<TreatmentResponse> getTreatment(UUID treatmentId) {
        return treatmentRepository.findOneById(treatmentId).map(TreatmentResponse::fromTreatmentResponse);
    }

    private Optional<Patient> getPatient(TreatmentDTO treatmentDTO) {
        var patient = patientService.findOneById(treatmentDTO.patientId());
        if (patient.isEmpty()) {
            logger.error("patient not found. It is necessary to have a patient to include the treatment");
            return Optional.empty();
        }
        return patient;
    }

    private List<Professional> getProfessional(TreatmentDTO treatmentDTO) {
        return professionalService.getAllProfessionalsByClinics(treatmentDTO.professionals());
    }
}
