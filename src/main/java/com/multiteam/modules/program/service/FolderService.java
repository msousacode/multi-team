package com.multiteam.modules.program.service;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.dto.FolderListDTO;
import com.multiteam.modules.program.dto.FolderPostDTO;
import com.multiteam.modules.program.dto.FolderPutDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProfessional;
import com.multiteam.modules.program.entity.FolderProgram;
import com.multiteam.modules.program.entity.FolderTreatment;
import com.multiteam.modules.program.repository.FolderProfessionalRepository;
import com.multiteam.modules.program.repository.FolderProgramRepository;
import com.multiteam.modules.program.repository.FolderRepository;
import com.multiteam.modules.program.repository.FolderTreatmentRepository;
import com.multiteam.modules.treatment.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {

    private final PatientService patientService;
    private final ProgramService programService;
    private final ProfessionalService professionalService;
    private final FolderRepository folderRepository;
    private final FolderProfessionalRepository professionalFolderRepository;
    private final FolderProgramRepository folderProgramRepository;
    private final FolderTreatmentRepository folderTreatmentRepository;

    public FolderService(
            PatientService patientService,
            ProfessionalService professionalService,
            ProgramService programService,
            FolderRepository folderRepository,
            FolderProfessionalRepository professionalFolderRepository,
            FolderProgramRepository folderProgramRepository,
            FolderTreatmentRepository folderTreatmentRepository) {
        this.patientService = patientService;
        this.programService = programService;
        this.professionalService = professionalService;
        this.folderRepository = folderRepository;
        this.professionalFolderRepository = professionalFolderRepository;
        this.folderProgramRepository = folderProgramRepository;
        this.folderTreatmentRepository = folderTreatmentRepository;
    }

    @Transactional
    public Boolean createFolder(UUID patientId, FolderPostDTO folderPostDTO) {

        if(folderPostDTO.professionals().isEmpty()) {
            throw new BadRequestException("Deve existir no mínimo 1 profissional vinculado a pasta curricular.");
        }

        var patient = patientService.getPatientById(patientId);

        Folder newFolder = new Folder();
        newFolder.setFolderName(folderPostDTO.folderName());
        newFolder.setActive(true);
        newFolder.setPatient(patient.get());

        var folder = folderRepository.save(newFolder);

        salveRelationshipFolderProfessional(folder, folderPostDTO.professionals());

        return Boolean.TRUE;
    }

    public Page<Folder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    public Optional<FolderListDTO> getFolderById(UUID folderId) {

        var professionals = professionalService.getProfessionalsFoldersById(folderId);

        var folder = folderRepository.findById(folderId);

        var folderProgram = folderProgramRepository.findAllByFolder_Id(folderId);

        var programs = folderProgram.stream().map(program -> program.getProgram()).toList();

        return Optional.ofNullable(new FolderListDTO(folder.get(), programs));
    }

    @Transactional
    public boolean updateFolder(UUID folderId, UUID patientId, FolderPutDTO folderDTO) {

        var folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        var patient = patientService.getPatientById(patientId);

        folder.setFolderName(folderDTO.folderName());
        folder.setPatient(patient.get());

        salveRelationshipFolderProfessional(folder, folderDTO.professionals());
        saveProgramas(folderDTO, folder);

        return true;
    }

    private void saveProgramas(FolderPutDTO folderDTO, Folder folder) {

        var uuids = folderDTO.programs();

        var programs = programService.findProgramsByIdInBacth(uuids);
        var foldersPrograms = programs.stream().map(program -> {
            var folderPrograms = new FolderProgram();
            folderPrograms.setFolder(folder);
            folderPrograms.setProgram(program);
            return folderPrograms;
        }).toList();

        folderProgramRepository.deleteByFolder_Id(folder.getId());

        folderProgramRepository.saveAll(foldersPrograms);
    }

    public List<Folder> getFolderByPatientId(UUID patientId) {
        var folders = folderRepository.findByPatient_Id(patientId);
        List<Folder> uniqueFolders = new ArrayList<>();
        folders.stream().distinct().forEach(uniqueFolders::add);
        return uniqueFolders;
    }

    @Transactional
    public void updateSituationFolder(List<UUID> folderIds, SituationEnum situation) {
        professionalFolderRepository.updateSituationFolder(folderIds, situation);
    }

    @Transactional
    public void createRelationshipFolderTreatment(List<UUID> folderIds, Treatment treatment) {
        var folders = folderRepository.findAllById(folderIds);
        List<FolderTreatment> folderTreatments = folders.stream().map(folder -> FolderTreatment.getInstance(folder, treatment)).toList();
        folderTreatmentRepository.saveAll(folderTreatments);
    }

    private void salveRelationshipFolderProfessional(Folder folder, List<Select> professionals) {

        var professionalsUUIDs = professionals.stream().map(p -> UUID.fromString(p.getCode())).toList();

        var professionalsResult = professionalService.getProfessionalsInBatch(professionalsUUIDs);

        var foldersProfessionals = professionalFolderRepository.findAllByFolder_Id(folder.getId());

        foldersProfessionals.forEach(fp -> {
            if(!professionalsUUIDs.contains(fp.getProfessional().getId())) {
                professionalFolderRepository.deleteById(fp.getId());
            }
        });

        professionalsResult.forEach(p -> {
            var isRelated = professionalFolderRepository.findByFolderIdAndProfessionalId(folder.getId(), p.getId());

            if(isRelated == 0) {
                FolderProfessional professionalFolder = new FolderProfessional();
                professionalFolder.setFolder(folder);
                professionalFolder.setProfessional(p);
                professionalFolder.setSituation(SituationEnum.NAO_ALOCADA);

                professionalFolderRepository.save(professionalFolder);
            }
        });
    }

    public List<Folder> getFoldersByIds(List<UUID> uuids) {
        return folderRepository.findAllById(uuids);
    }

    public List<Folder> fetchFoldersByPatient(UUID patientId) {
        return folderRepository.findByPatient_Id(patientId);
    }
}
