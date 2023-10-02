package com.multiteam.modules.program.service;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.dto.FolderDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProgram;
import com.multiteam.modules.program.entity.ProfessionalFolder;
import com.multiteam.modules.program.repository.FolderProgramRepository;
import com.multiteam.modules.program.repository.FolderRepository;
import com.multiteam.modules.program.repository.ProfessionalFolderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FolderService {

    private final PatientService patientService;
    private final ProgramService programService;
    private final ProfessionalService professionalService;
    private final FolderRepository folderRepository;
    private final ProfessionalFolderRepository professionalFolderRepository;
    private final FolderProgramRepository folderProgramRepository;

    public FolderService(
            PatientService patientService,
            ProfessionalService professionalService,
            ProgramService programService,
            FolderRepository folderRepository,
            ProfessionalFolderRepository professionalFolderRepository,
            FolderProgramRepository folderProgramRepository) {
        this.patientService = patientService;
        this.programService = programService;
        this.professionalService = professionalService;
        this.folderRepository = folderRepository;
        this.professionalFolderRepository = professionalFolderRepository;
        this.folderProgramRepository = folderProgramRepository;
    }

    @Transactional
    public Boolean createFolder(UUID patientId, FolderDTO folderDTO) {

        var patient = patientService.getPatientById(patientId);

        Folder folder = new Folder();
        folder.setFolderName(folderDTO.folderName());
        folder.setActive(true);
        folder.setPatient(patient.get());

        var folderSaved = folderRepository.save(folder);

        List<UUID> professionalsId = folderDTO.professionals().stream().map(p -> UUID.fromString(p.getCode())).toList();

        List<Professional> professionals = professionalService.getProfessionalsInBatch(professionalsId);

        professionals.forEach(professional -> {

            ProfessionalFolder professionalFolder = new ProfessionalFolder();
            professionalFolder.setFolder(folderSaved);
            professionalFolder.setProfessional(professional);
            professionalFolder.setSituation(SituationEnum.NAO_ALOCADA);

            professionalFolderRepository.save(professionalFolder);
        });

        return Boolean.TRUE;
    }

    public Page<Folder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    public Optional<FolderDTO> getFolderById(UUID folderId) {

        var professionals = professionalService.getProfessionalsFoldersById(folderId);

        List<Select> selects = professionals.stream().map(i -> Select.toSelect(i.getName(), i.getId().toString())).toList();

        var folder = folderRepository.findById(folderId);

        return Optional.ofNullable(new FolderDTO(folder.get(), selects));
    }

    @Transactional
    public boolean updateFolder(UUID folderId, FolderDTO folderDTO) {

        var folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        var programs = programService.findProgramsByIdInBacth(folderDTO.programs());

        var foldersPrograms = programs.stream().map(program -> {
            var folderPrograms = new FolderProgram();
            folderPrograms.setFolder(folder);
            folderPrograms.setProgram(program);
            return folderPrograms;
        }).toList();

        folderProgramRepository.saveAll(foldersPrograms);

        return true;
    }

    public List<Folder> getFolderByPatientId(UUID patientId) {
        return folderRepository.findByPatient_Id(patientId);
    }

    @Transactional
    public void updateSituationFolder(List<UUID> folderIds, SituationEnum situation) {
        professionalFolderRepository.updateSituationFolder(folderIds, situation);
    }
}
