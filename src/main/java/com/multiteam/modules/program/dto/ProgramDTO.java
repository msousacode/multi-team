package com.multiteam.modules.program.dto;

import java.util.UUID;

public record ProgramDTO(
        UUID programId,
        String programName,
        Integer mark,
        Integer ability,
        Integer protocol,
        Integer targetsBySession,
        Integer attemptBySession,
        String definition,
        String procedure,
        String learningCriterion,
        String materials,
        String observation,
        Integer correctionQuantityBySession,
        Integer correctionPercentageAttempts,
        Integer evolutionQuantityBySession,
        Integer evolutionPercentageAttempts,
        Integer code
) {
}

