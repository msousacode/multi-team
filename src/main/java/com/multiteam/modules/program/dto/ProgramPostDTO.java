package com.multiteam.modules.program.dto;

import com.multiteam.modules.program.entity.Program;

import java.util.UUID;

public record ProgramPostDTO(
        UUID programId,
        String programName,
        Integer ability,
        Integer protocol,
        String definition,
        String procedure,
        String learningCriterion,
        String materials,
        String observation,
        Integer code
) {
    public ProgramPostDTO(Program program) {
        this(
                program.getId(),
                program.getProgramName(),
                program.getAbility(),
                program.getProtocol(),
                program.getDefinition(),
                program.getProcedure(),
                program.getLearningCriterion(),
                program.getMaterials(),
                program.getObservation(),
                program.getCode()
        );
    }
}

