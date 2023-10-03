package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.modules.program.dto.ProgramListDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TreatmentListDTO(
        UUID id,
        TreatmentEnum situation,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate initialDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate finalDate,
        List<ProgramListDTO> programs
) {
}
