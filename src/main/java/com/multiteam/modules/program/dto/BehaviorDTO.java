package com.multiteam.modules.program.dto;

import java.time.LocalDateTime;

public record BehaviorDTO(
        String behaviorName,
        String orderExecution,
        String situation,
        String observation,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime acquiredDate,
        Integer maintenanceCount
) {
}
