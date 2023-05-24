package com.multiteam.modules.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleRequest(
        UUID id,
        UUID patientId,
        UUID clinicId,
        UUID professionalId,
        LocalDateTime start,
        LocalDateTime end,
        String description,
        Boolean active,
        String status,
        Boolean allDay
) {
}
