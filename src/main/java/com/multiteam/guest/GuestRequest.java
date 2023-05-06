package com.multiteam.guest;

import com.multiteam.core.enums.RelationshipEnum;

import java.util.UUID;

public record GuestRequest(
        UUID id,
        String name,
        String middleName,
        RelationshipEnum relationship,
        String cellPhone,
        String email,
        boolean active,
        UUID patientId,
        UUID ownerId
) {
}
