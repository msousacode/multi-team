package com.multiteam.modules.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleRequest(
        UUID patientId,
        UUID clientId,
        UUID professionalId,
        String title,
        LocalDateTime start,
        LocalDateTime end,
        String url,
        String description,
        String color,
        Boolean active,
        String status,
        Boolean allDay
) {
}
