package com.multiteam.modules.program.service;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.dto.FolderListDTO;
import com.multiteam.modules.program.dto.FolderPostDTO;
import com.multiteam.modules.program.dto.FolderPutDTO;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProfessional;
import com.multiteam.modules.program.entity.FolderProgram;
import com.multiteam.modules.program.repository.FolderProfessionalRepository;
import com.multiteam.modules.program.repository.FolderProgramRepository;
import com.multiteam.modules.program.repository.FolderRepository;
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

    public FolderService(
            PatientService patientService,
            ProfessionalService professionalService,
            ProgramService programService,
            FolderRepository folderRepository,
            FolderProfessionalRepository professionalFolderRepository,
            FolderProgramRepository folderProgramRepository) {
        this.patientService = patientService;
        this.programService = programService;
        this.professionalService = professionalService;
        this.folderRepository = folderRepository;
        this.professionalFolderRepository = professionalFolderRepository;
        this.folderProgramRepository = folderProgramRepository;
    }

    @Transactional
    public Boolean createFolder(UUID patientId, FolderPostDTO folderPostDTO) {

        var patient = patientService.getPatientById(patientId);

        Folder folder = new Folder();
        folder.setFolderName(folderPostDTO.folderName());
        folder.setActive(true);
        folder.setPatient(patient.get());

        var folderSaved = folderRepository.save(folder);

        salveRelationshipProfessionalFolder(folderSaved, folderPostDTO.professionals());

        return Boolean.TRUE;
    }

    public Page<Folder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    public Optional<FolderListDTO> getFolderById(UUID folderId) {

        var professionals = professionalService.getProfessionalsFoldersById(folderId);

        List<Select> selects = professionals.stream().map(i -> Select.toSelect(i.getName(), i.getId().toString())).toList();

        var folder = folderRepository.findById(folderId);

        var folderProgram = folderProgramRepository.findAllByFolder_Id(folderId);

        var programs = folderProgram.stream().map(program -> program.getProgram()).toList();

        return Optional.ofNullable(new FolderListDTO(folder.get(), selects, programs));
    }

    @Transactional
    public boolean updateFolder(UUID folderId, UUID patientId, FolderPutDTO folderDTO) {

        var folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        var patient = patientService.getPatientById(patientId);

        folder.setFolderName(folderDTO.folderName());
        folder.setPatient(patient.get());

        salveRelationshipProfessionalFolder(folder, folderDTO.professionals());
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
        return folderRepository.findByPatient_Id(patientId);
    }

    @Transactional
    public void updateSituationFolder(List<UUID> folderIds, SituationEnum situation) {
        professionalFolderRepository.updateSituationFolder(folderIds, situation);
    }

    @Transactional
    public void updateRelationshipFolderProfessional(List<UUID> folderIds, Treatment treatment) {
        var folders = folderRepository.findAllById(folderIds);
        folders.forEach(folder -> folder.setTreatment(treatment));
        folderRepository.saveAll(folders);
    }

    private void salveRelationshipProfessionalFolder(Folder folder, List<Select> selects) {
        List<UUID> uuids = selects.stream().map(p -> UUID.fromString(p.getCode())).toList();
        List<Professional> professionals = professionalService.getProfessionalsInBatch(uuids);
        professionals.forEach(professional -> {
            FolderProfessional professionalFolder = new FolderProfessional();
            professionalFolder.setFolder(folder);
            professionalFolder.setProfessional(professional);
            professionalFolder.setSituation(SituationEnum.NAO_ALOCADA);

            professionalFolderRepository.deleteByFolder_Id(folder.getId());
            professionalFolderRepository.save(professionalFolder);
        });
    }
}
