package com.multiteam.modules.treatment;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.annotation.AnnotationService;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.service.FolderService;
import com.multiteam.modules.treatment.dto.TreatmentEditResponse;
import com.multiteam.modules.treatment.dto.TreatmentSearch;
import com.multiteam.modules.treatment.dto.TreatmentPostDTO;
import com.multiteam.modules.treatment.dto.TreatmentResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TreatmentService {

    private static final Logger logger = LogManager.getLogger(TreatmentService.class);

    private final TreatmentRepository treatmentRepository;
    private final TreatementProfessionalRepository treatmentProfessionalRepository;
    private final PatientService patientService;
    private final ProfessionalService professionalService;
    private final AnnotationService annotationService;
    private final FolderService folderService;

    public TreatmentService(
            TreatmentRepository treatmentRepository,
            TreatementProfessionalRepository treatementProfessionalRepository,
            @Lazy PatientService patientService,
            @Lazy ProfessionalService professionalService,
            @Lazy AnnotationService annotationService,
            @Lazy FolderService folderService) {
        this.treatmentRepository = treatmentRepository;
        this.treatmentProfessionalRepository = treatementProfessionalRepository;
        this.patientService = patientService;
        this.professionalService = professionalService;
        this.annotationService = annotationService;
        this.folderService = folderService;
    }

    @Transactional
    public Boolean createTreatment(UUID patientId, final TreatmentPostDTO treatmentDTO) {

        var patient = patientService.getPatientById(patientId);

        List<UUID> folderIds = new ArrayList<>();
        treatmentDTO.folders().forEach(i -> folderIds.add(UUID.fromString(i.getCode())));

        folderService.updateSituationFolder(folderIds, SituationEnum.EM_COLETA);

        var builder = new Treatment.Builder(
                null,
                SituationEnum.ANDAMENTO,
                treatmentDTO.initialDate(),
                patient.get())
                .finalDate(treatmentDTO.finalDate())
                .description(treatmentDTO.observation())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        //Salva o vínculo entre Tratamento, Profissional e Clínica
        //saveRelationshipTreatmentProfessional(professionals, treatment);

        logger.info("successfully included treatment id: {}", treatment.getId());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(final TreatmentPostDTO treatmentRequest) {

        var treatment = treatmentRepository.findById(treatmentRequest.id());

        if (treatment.isEmpty()) {
            logger.error("error when updating treatment id: {}", treatmentRequest.id());
            return Boolean.FALSE;
        }

        //var patient = getPatient(treatmentRequest);
//
        //if (patient.isEmpty() || professionals.isEmpty()) {
        //    logger.error("patient or professional cannot be empty");
        //    throw new BadRequestException("patient or professional cannot be empty or null");
        //}

        var builder = new Treatment.Builder(
                treatment.get().getId(),
                SituationEnum.get(treatmentRequest.situation()),
                treatmentRequest.initialDate(),
                treatment.get().getPatient())
                .description(treatmentRequest.observation())
                .finalDate(treatmentRequest.finalDate())
                .active(treatment.get().isActive())
                .build();

        //inactive professionals of treatment
        treatmentProfessionalRepository.inactiveRelationshipTreatementAndProfessionalByTreatment_Id(SituationEnum.INATIVO, treatment.get().getId());

        var treatmentSaved = treatmentRepository.save(builder);

        //saveRelationshipTreatmentProfessional(professionals, treatmentSaved);

        logger.info("successfully updated treatment");

        return Boolean.TRUE;
    }

    public Page<TreatmentResponse> getAllTreatments(final TreatmentSearch filter, Pageable pageable) {
        if (filter.patientId() != null) {
            return treatmentRepository.findAllByPatient_IdAndActiveIsTrue(filter.patientId(), pageable).map(TreatmentResponse::fromTreatmentResponse);
        }
        return treatmentRepository.findAllByPatient_NameContainingIgnoreCaseAndActiveIsTrue(filter.patientName(), pageable).map(TreatmentResponse::fromTreatmentResponse);
    }

    @Transactional
    public Boolean inactiveTreatment(UUID treatmentId) {

        var treatment = treatmentRepository.findById(treatmentId);

        if (treatment.isEmpty()) {
            return Boolean.FALSE;
        }

        //inactive professionals of treatment
        treatmentProfessionalRepository.inactiveRelationshipTreatementAndProfessionalByTreatment_Id(SituationEnum.INATIVO, treatmentId);

        //exclude all guests TODO será usado futuramente release 2 quando houver a funcionalidade de convidados
        //treatment.get().getGuests().removeAll(treatment.get().getGuests());
        //treatmentRepository.save(treatment.get());

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
            if(treatmentProfessional.getActive()) {
                professionals.add(treatmentProfessional.getProfessional().getId());
            }
            //get clinics utilizando o clinicId da clinica em que o tratamento esta alocado.
            clinics.addAll(treatmentProfessional.getProfessional().getClinics());
        });

        //pequisa as clinicas e alimenta o objeto clinics.addAll()

        return Optional.of(TreatmentEditResponse.fromTreatmentEditResponse(treatment.get(), clinics, professionals));
    }

    private void saveRelationshipTreatmentProfessional(List<Professional> professionals, Treatment treatment) {
        professionals.forEach(professional -> {
            professional.getClinics().forEach(clinic -> {
                var treatmentProfessional = new TreatmentProfessional(null, treatment, professional, clinic, SituationEnum.NAO_ALOCADA);
                treatmentProfessionalRepository.save(treatmentProfessional);
            });
        });
    }

    public Optional<Treatment> findTreatment(UUID treatmentId) {
        return treatmentRepository.findById(treatmentId);
    }
}
