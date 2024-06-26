package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.mapper.ProgramPostMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record FolderListDTO(
        UUID folderId,
        Integer code,
        String folderName,
        String patientName,
        UUID patientId,
        Boolean active,
        SituationEnum situation,
        Set<ProgramDTO> programs,
        List<Select> professionals,
        UUID treatmentId
) {

    public FolderListDTO(Folder folder, List<Program> programs) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().get(0).getSituation(),
                programs.stream().map(program -> new ProgramDTO(program)).collect(Collectors.toSet()),
                selectProfessionals(folder),
                null
        );
    }

    public static List<Select> selectProfessionals(Folder folder) {
        return folder.getFolderProfessional().stream().map(i -> Select.toSelect(i.getProfessional().getName(), i.getProfessional().getId().toString())).toList();
    }

    public FolderListDTO(Folder folder) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().isEmpty() ? null : folder.getFolderProfessional().get(0).getSituation(),
                folder.getFolderPrograms().stream().map(program -> ProgramPostMapper.MAPPER.toDTO(program.getProgram())).collect(Collectors.toSet()),
                selectProfessionals(folder),
                getId(folder)
        );
    }

    private static UUID getId(Folder folder) {
        return !folder.getFolderTreatments().isEmpty() ? folder.getFolderTreatments().stream().filter(folderTreatment -> folder.getId() == folderTreatment.getFolder().getId()).findFirst().get().getTreatment().getId() : null;
    }
}

