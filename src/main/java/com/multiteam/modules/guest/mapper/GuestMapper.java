package com.multiteam.modules.guest.mapper;

import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.guest.dto.GuestPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GuestMapper {

    GuestMapper MAPPER = Mappers.getMapper(GuestMapper.class);

    Guest toEntity(GuestPostDTO guestPostDTO);
    
    GuestPostDTO toDTO(Guest guest);
}
