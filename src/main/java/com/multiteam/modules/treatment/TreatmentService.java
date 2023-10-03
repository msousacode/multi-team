package com.multiteam.modules.treatment;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.modules.annotation.AnnotationService;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.service.FolderService;
import com.multiteam.modules.treatment.dto.TreatmentEditResponse;
import com.multiteam.modules.treatment.dto.TreatmentSearchDTO;
import com.multiteam.modules.treatment.dto.TreatmentPostDTO;
import com.multiteam.modules.treatment.dto.TreatmentListDTO;
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
    private final PatientService patientService;
    private final FolderService folderService;

    public TreatmentService(
            TreatmentRepository treatmentRepository,
            @Lazy PatientService patientService,
            @Lazy FolderService folderService) {
        this.treatmentRepository = treatmentRepository;
        this.patientService = patientService;
        this.folderService = folderService;
    }

    @Transactional
    public Boolean createTreatment(UUID patientId, final TreatmentPostDTO treatmentDTO) {

        var patient = patientService.getPatientById(patientId);

        var builder = new Treatment.Builder(
                null,
                TreatmentEnum.get(treatmentDTO.situation()),
                treatmentDTO.initialDate(),
                patient.get())
                .finalDate(treatmentDTO.finalDate())
                .description(treatmentDTO.observation())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(builder);

        List<UUID> folderIds = new ArrayList<>();
        treatmentDTO.folders().forEach(i -> folderIds.add(UUID.fromString(i.getCode())));

        folderService.updateSituationFolder(folderIds, SituationEnum.EM_COLETA);

        folderService.updateRelationshipFolderProfessional(folderIds, treatment);

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

        var builder = new Treatment.Builder(
                treatment.get().getId(),
                TreatmentEnum.get(treatmentRequest.situation()),
                treatmentRequest.initialDate(),
                treatment.get().getPatient())
                .description(treatmentRequest.observation())
                .finalDate(treatmentRequest.finalDate())
                .active(treatment.get().isActive())
                .build();

        logger.info("successfully updated treatment");

        return Boolean.TRUE;
    }

    public Page<TreatmentListDTO> getAllTreatments(final TreatmentSearchDTO filter, Pageable pageable) {

        //Buscar Tratatamentos vinculados aos programas.

        if (filter.patientId() != null) {
            return treatmentRepository.findAllByPatient_IdAndActiveIsTrue(filter.patientId(), pageable).map(TreatmentListDTO::new);

        }
        //return treatmentRepository.findAllByPatient_NameContainingIgnoreCaseAndActiveIsTrue(filter.patientName(), pageable).map(TreatmentResponse::fromTreatmentResponse);
        return Page.empty();
    }

    @Transactional
    public Boolean inactiveTreatment(UUID treatmentId) {

        var treatment = treatmentRepository.findById(treatmentId);

        if (treatment.isEmpty()) {
            return Boolean.FALSE;
        }

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

        return Optional.of(TreatmentEditResponse.fromTreatmentEditResponse(treatment.get(), clinics, professionals));
    }
}
