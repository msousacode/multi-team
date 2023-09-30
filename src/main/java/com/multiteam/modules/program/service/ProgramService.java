package com.multiteam.modules.program.service;

import com.multiteam.core.enums.AbilityEnum;
import com.multiteam.core.enums.ProtocolEnum;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.professional.Professional;
import com.multiteam.modules.professional.ProfessionalRepository;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.program.dto.FolderDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.ProfessionalFolder;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.mapper.ProgramMapper;
import com.multiteam.modules.program.repository.FolderRepository;
import com.multiteam.modules.program.repository.ProfessionalFolderRepository;
import com.multiteam.modules.program.repository.ProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProgramService {

    private final PatientService patientService;
    private final ProfessionalService professionalService;
    private final ProgramRepository programRepository;
    private final FolderRepository folderRepository;
    private final ProfessionalFolderRepository professionalFolderRepository;

    public ProgramService(
            PatientService patientService,
            ProfessionalService professionalService,
            ProgramRepository programRepository,
            FolderRepository folderRepository,
            ProfessionalFolderRepository professionalFolderRepository) {
        this.patientService = patientService;
        this.professionalService = professionalService;
        this.programRepository = programRepository;
        this.folderRepository = folderRepository;
        this.professionalFolderRepository = professionalFolderRepository;
    }

    @Transactional
    public boolean createProgram(ProgramDTO programDTO) {

        Program program = ProgramMapper.MAPPER.toEntity(programDTO);

        program.setAbility(AbilityEnum.get(program.getAbility()).getValue());
        program.setProtocol(ProtocolEnum.get(program.getProtocol()).getValue());
        program.setActive(true);

        programRepository.save(program);

        return Boolean.TRUE;
    }

    public Page<Program> getAll(Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    public Optional<Program> getById(UUID programId) {
        return programRepository.findById(programId);
    }

    @Transactional
    public Boolean updateProgram(ProgramDTO programDTO) {

        var optional = getById(programDTO.programId());

        if(optional.isPresent()) {
            var result = ProgramMapper.MAPPER.toEntity(programDTO);
            result.setId(programDTO.programId());
            result.setActive(true);
            programRepository.save(result);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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

            professionalFolderRepository.save(professionalFolder);
        });

        return Boolean.TRUE;
    }
}
