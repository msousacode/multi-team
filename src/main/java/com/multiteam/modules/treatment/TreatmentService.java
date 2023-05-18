package com.multiteam.modules.treatment;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.clinic.ClinicDTO;
import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.dto.TreatmentEditResponse;
import com.multiteam.modules.treatment.dto.TreatmentFilter;
import com.multiteam.modules.treatment.dto.TreatmentRequest;
import com.multiteam.modules.treatment.dto.TreatmentResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
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
    public Boolean createTreatment(final TreatmentRequest treatmentDTO) {
        logger.info("include treatment to patient id {}", treatmentDTO.patientId());
        var patient = getPatient(treatmentDTO);
        var professionals = getProfessional(treatmentDTO);//TODO Buscar profissionais

        if (patient.isEmpty() || professionals.isEmpty()) {
            logger.error("patient or professional cannot be empty");
            throw new BadRequestException("patient or professional cannot be empty");
        }

        var builder = new Treatment.Builder(
                null,
                SituationEnum.get(treatmentDTO.situation()),//TODO Ajustar para conveter a partir do DTO
                treatmentDTO.initialDate(),
                patient.get())
                .finalDate(treatmentDTO.finalDate())
                .description(treatmentDTO.observation())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        //Salva o vínculo entre Tratamento, Profissional e Clínica
        saveLinkTreatmentProfessional(professionals, treatment);

        logger.info("successfully included treatment id: {}", treatment.getId());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(final TreatmentRequest treatmentRequest) {

        var result = treatmentRepository.findById(treatmentRequest.id());

        if (result.isEmpty()) {
            logger.error("error when updating treatment id: {}", treatmentRequest.id());
            return Boolean.FALSE;
        }

        var patient = getPatient(treatmentRequest);
        var professionals = getProfessional(treatmentRequest);

        if (patient.isEmpty() || professionals.isEmpty()) {
            logger.error("patient or professional cannot be empty");
            throw new BadRequestException("patient or professional cannot be empty or null");
        }

        var builder = new Treatment.Builder(
                result.get().getId(),
                SituationEnum.get(treatmentRequest.situation()),
                treatmentRequest.initialDate(),
                result.get().getPatient())
                .description(treatmentRequest.observation())
                .finalDate(treatmentRequest.finalDate())
                .build();

        var treatment = treatmentRepository.save(builder);

        saveLinkTreatmentProfessional(professionals, treatment);

        logger.info("successfully updated treatment");

        return Boolean.TRUE;
    }

    public Page<TreatmentResponse> getAllTreatments(final TreatmentFilter filter, Pageable pageable) {

        if (filter.patientId() != null) {
            return treatmentRepository.findAllByPatient_Id(filter.patientId(), pageable).map(TreatmentResponse::fromTreatmentResponse);
        }
        return treatmentRepository.findAll(pageable).map(TreatmentResponse::fromTreatmentResponse);
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
    public void includeGuestInTreatment(final Guest guest, final Set<Treatment> treatments) {
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
    public void excludeTreatmentByPatientId(final UUID patientId) {
        var treatments = treatmentRepository.findAllTreatments(patientId);
        treatments.forEach(t -> inactiveTreatment(t.getId()));
    }

    public Optional<TreatmentEditResponse> getTreatment(final UUID treatmentId) {
        var treatment = treatmentRepository.findOneById(treatmentId);

        if (treatment.isEmpty()) {
            logger.error("treatment not found id: {}", treatmentId);
            return Optional.empty();
        }

        Set<UUID> professionals = new HashSet<>();
        Set<Clinic> clinics = new HashSet<>();

        var treatmentProfessionals = treatment.get().getTreatmentProfessionals();

        treatmentProfessionals.forEach(treatmentProfessional -> {
            professionals.add(treatmentProfessional.getProfessional().getId());
            //get clinics utilizando o clinicId da clinica em que o tratamento esta alocado.
            clinics.addAll(treatmentProfessional.getProfessional().getClinics());
        });

        //pequisa as clinicas e alimenta o objeto clinics.addAll()

        return Optional.of(TreatmentEditResponse.fromTreatmentEditResponse(treatment.get(), clinics, professionals));
    }

    private Optional<Patient> getPatient(final TreatmentRequest treatmentDTO) {
        var patient = patientService.findOneById(treatmentDTO.patientId());
        if (patient.isEmpty()) {
            logger.error("patient not found. It is necessary to have a patient to include the treatment");
            return Optional.empty();
        }
        return patient;
    }

    private List<Professional> getProfessional(final TreatmentRequest treatmentDTO) {
        return professionalService.getAllProfessionalsByClinics(treatmentDTO.professionals());
    }

    private void saveLinkTreatmentProfessional(List<Professional> professionals, Treatment treatment) {

        treatementProfessionalRepository.deleteByTreatment_Id(treatment.getId());

        professionals.forEach(professional -> {
            professional.getClinics().forEach(clinic -> {
                var treatmentProfessional = new TreatmentProfessional(null, treatment, professional, clinic, SituationEnum.ANDAMENTO);
                treatementProfessionalRepository.save(treatmentProfessional);
            });
        });
    }
}
