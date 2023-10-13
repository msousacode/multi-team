package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.mapper.ProgramMapper;

import java.util.List;
import java.util.UUID;

public record FolderListDTO(
        UUID folderId,
        Integer code,
        String folderName,
        String patientName,
        UUID patientId,
        Boolean active,
        String situation,
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
                folder.getFolderProfessional().get(0).getSituation().getDescription(),
                List.of(),
                SelectProfessionals(folder));
    }

    public FolderListDTO(Folder folder, List<Program> programs) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().get(0).getSituation().getDescription(),
                programs.stream().map(program -> ProgramMapper.MAPPER.toDTO(program)).toList(),
                SelectProfessionals(folder)
        );
    }

    private static List<Select> SelectProfessionals(Folder folder) {
        return folder.getFolderProfessional().stream().map(i -> Select.toSelect(i.getProfessional().getName(), i.getProfessional().getId().toString())).toList();
    }
}

