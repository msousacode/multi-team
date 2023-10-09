package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProfessional;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.mapper.ProgramMapper;

import java.util.List;
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
        List<ProgramDTO> programs,
        List<Select> professionals
) {
    public FolderListDTO(Folder folder) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().get(0).getSituation(),
                List.of(),
                List.of());
    }

    public FolderListDTO(Folder folder, List<Select> selects, List<Program> programs) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().get(0).getSituation(),
                programs.stream().map(program -> ProgramMapper.MAPPER.toDTO(program)).toList(),
                selects
        );
    }

    private static String toConcat(List<FolderProfessional> folderProfessional) {
        return folderProfessional.stream().map(professional -> professional.getProfessional().getName()).collect(Collectors.joining(", "));
    }
}

