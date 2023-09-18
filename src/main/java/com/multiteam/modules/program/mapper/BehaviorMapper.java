package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.Behavior;
import com.multiteam.modules.program.Program;
import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.dto.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BehaviorMapper {

    BehaviorMapper MAPPER = Mappers.getMapper(BehaviorMapper.class);

    Behavior toEntity(BehaviorDTO behaviorDTO);

    BehaviorDTO toDTO(Behavior behavior);
}
