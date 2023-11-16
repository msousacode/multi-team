package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.modules.program.dto.FolderListBasicInformationDTO;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TreatmentListDTO(
        UUID id,
        Integer code,
        TreatmentEnum situation,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate initialDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate finalDate,
        List<FolderListBasicInformationDTO> folders
) {
    public TreatmentListDTO(Treatment treatment) {
        this(
                treatment.getId(),
                treatment.getCode(),
                treatment.getSituation(),
                treatment.getInitialDate(),
                treatment.getFinalDate(),
                treatment.getFolders().stream().map(FolderListBasicInformationDTO::new).toList()
        );
    }
}
