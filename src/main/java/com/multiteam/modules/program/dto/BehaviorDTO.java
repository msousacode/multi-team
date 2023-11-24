package com.multiteam.modules.program.dto;

import com.multiteam.modules.program.entity.BehaviorCollect;

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
        Integer maintenanceCount,
        Boolean time,
        String question,
        String response,
        UUID patientId,
        UUID programId

) {
    public BehaviorDTO(BehaviorCollect behavior) {
        this(
                behavior.getId(),
                behavior.getBehaviorName(),
                behavior.getOrderExecution(),
                behavior.getSituation(),
                behavior.getObservation(),
                behavior.getStartDate(),
                behavior.getEndDate(),
                behavior.getAcquiredDate(),
                behavior.getMaintenanceCount(),
                behavior.getTime(),
                behavior.getQuestion(),
                behavior.getResponse(),
                behavior.getId(),
                behavior.getBehavior().getProgram().getId()
        );
    }
}
