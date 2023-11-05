package com.multiteam.modules.program.dto;

import com.multiteam.modules.program.entity.Program;

import java.util.List;
import java.util.UUID;

public record ProgramDTO(
        UUID programId,
        String programName,
        Integer ability,
        Integer protocol,
        Integer attemptBySession,
        String definition,
        String procedure,
        String learningCriterion,
        String materials,
        String observation,
        Integer code,
        List<BehaviorDTO> behaviors
) {
    public ProgramDTO(Program program) {
        this(
                program.getId(),
                program.getProgramName(),
                program.getAbility(),
                program.getProtocol(),
                program.getAttemptBySession(),
                program.getDefinition(),
                program.getProcedure(),
                program.getLearningCriterion(),
                program.getMaterials(),
                program.getObservation(),
                program.getCode(),
                program.getBehaviors().stream().map(BehaviorDTO::new).toList()
        );
    }
}

