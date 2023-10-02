package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.dto.BehaviorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BehaviorMapper {

    BehaviorMapper MAPPER = Mappers.getMapper(BehaviorMapper.class);

    Behavior toEntity(BehaviorDTO behaviorDTO);

    BehaviorDTO toDTO(Behavior behavior);
}
