package com.multiteam.modules.program.dto;

import java.util.UUID;

public record BehaviorResponseCollectDTO(
        UUID behaviorId,
        UUID patientId,
        String response
        ) {
}
