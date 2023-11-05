package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.dto.ProgramPostDTO;
import com.multiteam.modules.program.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgramPostMapper {

    ProgramPostMapper MAPPER = Mappers.getMapper(ProgramPostMapper.class);

    Program toEntity(ProgramPostDTO programDTO);

    @Mapping(source = "id", target = "programId")
    ProgramDTO toDTO(Program program);
}
