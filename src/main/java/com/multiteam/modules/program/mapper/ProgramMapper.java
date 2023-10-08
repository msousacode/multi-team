package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.dto.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgramMapper {

    ProgramMapper MAPPER = Mappers.getMapper(ProgramMapper.class);

    Program toEntity(ProgramDTO programDTO);

    @Mapping(source = "id", target = "programId")
    ProgramDTO toDTO(Program program);
}
