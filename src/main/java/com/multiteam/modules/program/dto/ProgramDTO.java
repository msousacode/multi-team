package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.AbilityEnum;
import com.multiteam.core.enums.ProtocolEnum;
import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.mapper.BehaviorCollectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProgramDTO(
        UUID programId,
        String programName,
        String ability,
        String protocol,
        Integer attemptBySession,
        String definition,
        String procedure,
        String learningCriterion,
        String materials,
        String observation,
        Integer code
) {
    public ProgramDTO(Program program) {
        this(
                program.getId(),
                program.getProgramName(),
                AbilityEnum.get(program.getAbility()).getDescription(),
                ProtocolEnum.get(program.getProtocol()).getDescription(),
                program.getAttemptBySession(),
                program.getDefinition(),
                program.getProcedure(),
                program.getLearningCriterion(),
                program.getMaterials(),
                program.getObservation(),
                program.getCode()
        );
    }
}

