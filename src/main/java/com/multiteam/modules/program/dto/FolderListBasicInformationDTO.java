package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;

import java.util.List;
import java.util.UUID;

public record FolderListBasicInformationDTO(
        UUID folderId,
        Integer code,
        String folderName,
        String patientName,
        UUID patientId,
        Boolean active,
        SituationEnum situation,
        List<Select> professionals
) {
    public FolderListBasicInformationDTO(Folder folder) {
        this(
                folder.getId(),
                folder.getCode(),
                folder.getFolderName(),
                folder.getPatient().getName(),
                folder.getPatient().getId(),
                folder.getActive(),
                folder.getFolderProfessional().isEmpty() ? null : folder.getFolderProfessional().get(0).getSituation(),
                selectProfessionals(folder));
    }

    public static List<Select> selectProfessionals(Folder folder) {
        return folder.getFolderProfessional().stream().map(i -> Select.toSelect(i.getProfessional().getName(), i.getProfessional().getId().toString())).toList();
    }
}

