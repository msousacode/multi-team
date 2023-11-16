package com.multiteam.modules.treatment;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.service.FolderService;
import com.multiteam.modules.treatment.dto.TreatmentEditDTO;
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
    public Boolean createTreatment(UUID patientId, TreatmentPostDTO treatmentPostDTO) {

        var patient = patientService.getPatientById(patientId);

        var treatmentBuilder = new Treatment.Builder(
                null,
                TreatmentEnum.get(treatmentPostDTO.situation()),
                treatmentPostDTO.initialDate(),
                patient.get())
                .finalDate(treatmentPostDTO.finalDate())
                .description(treatmentPostDTO.observation())
                .active(true)
                .build();

        var treatment = treatmentRepository.save(treatmentBuilder);

        var foldersAllocated = treatmentPostDTO.foldersAllocated().stream().map(folder -> UUID.fromString(folder.getCode())).toList();
        var foldersUnallocated = treatmentPostDTO.foldersUnallocated().stream().map(folder -> UUID.fromString(folder.getCode())).toList();

        folderService.createRelationshipFolderTreatment(foldersAllocated, treatment);
        folderService.createRelationshipFolderTreatment(foldersUnallocated, treatment);

        folderService.updateSituationFolder(foldersAllocated, SituationEnum.EM_COLETA);
        folderService.updateSituationFolder(foldersUnallocated, SituationEnum.NAO_ALOCADA);

        logger.info("successfully included treatment id: {}", treatment.getId());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateTreatment(TreatmentPostDTO treatmentPostDTO) {

        var treatment = treatmentRepository.findById(treatmentPostDTO.id());

        if (treatment.isEmpty()) {
            logger.error("error when updating treatment id: {}", treatmentPostDTO.id());
            return Boolean.FALSE;
        }

        var foldersAllocated = treatmentPostDTO.foldersAllocated().stream().map(folder -> UUID.fromString(folder.getCode())).toList();
        var foldersUnallocated = treatmentPostDTO.foldersUnallocated().stream().map(folder -> UUID.fromString(folder.getCode())).toList();

        treatment.get().setSituation(TreatmentEnum.get(treatmentPostDTO.situation()));
        treatment.get().setDescription(treatmentPostDTO.observation());
        treatment.get().setInitialDate(treatmentPostDTO.initialDate());
        treatment.get().setFinalDate(treatmentPostDTO.finalDate());

        treatmentRepository.save(treatment.get());

        folderService.createRelationshipFolderTreatment(foldersAllocated, treatment.get());
        folderService.createRelationshipFolderTreatment(foldersUnallocated, treatment.get());

        folderService.updateSituationFolder(foldersAllocated, SituationEnum.EM_COLETA);
        folderService.updateSituationFolder(foldersUnallocated, SituationEnum.NAO_ALOCADA);

        logger.info("successfully updated treatment");

        return Boolean.TRUE;
    }

    public Page<TreatmentListDTO> getAllTreatments(final TreatmentSearchDTO filter, Pageable pageable) {
        return treatmentRepository.findAllByPatient_IdAndActiveIsTrue(filter.patientId(), pageable).map(TreatmentListDTO::new);
        //return treatmentRepository.findAllByPatient_NameContainingIgnoreCaseAndActiveIsTrue(filter.patientName(), pageable).map(TreatmentResponse::fromTreatmentResponse);
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

    public Optional<TreatmentEditDTO> getTreatment(final UUID treatmentId) {
        var treatment = treatmentRepository.findOneById(treatmentId);

        if (treatment.isEmpty()) {
            logger.error("treatment not found id: {}", treatmentId);
            return Optional.empty();
        }

        Set<UUID> professionals = new HashSet<>();
        Set<Clinic> clinics = new HashSet<>();
        List<Folder> folders = treatment.get().getFolders().stream().toList();

        return Optional.of(TreatmentEditDTO.toDTO(treatment.get(), clinics, professionals, folders));
    }
}
