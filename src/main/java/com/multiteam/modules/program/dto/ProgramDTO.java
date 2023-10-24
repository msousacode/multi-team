package com.multiteam.modules.program.dto;

import com.multiteam.modules.program.entity.Program;

import java.util.List;
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
        Integer code,
        List<BehaviorDTO> behaviors
) {
    public ProgramDTO(Program program) {
        this(
                program.getId(),
                program.getProgramName(),
                program.getMark(),
                program.getAbility(),
                program.getProtocol(),
                program.getTargetsBySession(),
                program.getAttemptBySession(),
                program.getDefinition(),
                program.getProcedure(),
                program.getLearningCriterion(),
                program.getMaterials(),
                program.getObservation(),
                program.getCorrectionQuantityBySession(),
                program.getEvolutionPercentageAttempts(),
                program.getEvolutionQuantityBySession(),
                program.getEvolutionPercentageAttempts(),
                program.getCode(),
                program.getBehaviors().stream().map(BehaviorDTO::new).toList()
        );
    }
}

