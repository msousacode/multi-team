package com.multiteam.modules.program.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BehaviorDTO(
        UUID id,
        String behaviorName,
        Integer orderExecution,
        Integer situation,
        String observation,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime acquiredDate,
        Integer maintenanceCount
) {
}
