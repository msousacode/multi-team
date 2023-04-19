package com.multiteam.controller.dto.request;

import com.multiteam.persistence.enums.RelationshipType;

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
