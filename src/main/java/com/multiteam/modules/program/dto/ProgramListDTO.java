package com.multiteam.modules.program.dto;

import com.multiteam.core.enums.AbilityEnum;
import com.multiteam.core.enums.ProtocolEnum;
import com.multiteam.modules.program.entity.Program;

import java.util.UUID;

public record ProgramListDTO(
        UUID programId,
        Integer code,
        String programName,
        String ability,
        String protocol
) {
    public ProgramListDTO(Program program) {
        this(
                program.getId(),
                program.getCode(),
                program.getProgramName(),
                AbilityEnum.get(program.getAbility()).getDescription(),
                ProtocolEnum.get(program.getProtocol()).getDescription()
        );
    }
}

