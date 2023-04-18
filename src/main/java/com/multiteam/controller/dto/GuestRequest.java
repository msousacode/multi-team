package com.multiteam.controller.dto;

import com.multiteam.persistence.types.RelationshipType;

import java.util.UUID;

public record GuestRequest(
        UUID id,
        String name,
        String middleName,
        RelationshipType relationship,
        String cellPhone,
        String email,
        boolean active,
        UUID patientId
) {
}
