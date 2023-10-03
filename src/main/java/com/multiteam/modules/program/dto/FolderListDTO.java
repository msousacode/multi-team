package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProfessional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record FolderListDTO(
        UUID folderId,
        String folderName,
        SituationEnum situation,
        String professionals
) {
    public FolderListDTO(Folder folder) {
        this(folder.getId(), folder.getFolderName(), folder.getFolderProfessional().get(0).getSituation(), toConcat(folder.getFolderProfessional()));
    }

    private static String toConcat(List<FolderProfessional> folderProfessional) {
        return folderProfessional.stream().map(professional -> professional.getProfessional().getName()).collect(Collectors.joining(", "));
    }
}

