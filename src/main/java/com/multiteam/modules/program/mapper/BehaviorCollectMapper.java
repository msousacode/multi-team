package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.entity.BehaviorCollect;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BehaviorCollectMapper {

    BehaviorCollectMapper MAPPER = Mappers.getMapper(BehaviorCollectMapper.class);

    @Mapping(target = "id", ignore = true)
    BehaviorCollect toEntity(Behavior behavior);

    @Mapping(target = "behaviorId", source = "id")
    @Mapping(target = "patientId", source = "patient.id")
    BehaviorDTO toDTO(BehaviorCollect behaviorCollect);
}
