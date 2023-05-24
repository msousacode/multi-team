package com.multiteam.modules.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleRequest(
        UUID patientId,
        UUID clinicId,
        UUID professionalId,
        LocalDateTime start,
        LocalDateTime end,
        String url,
        String description,
        Boolean active,
        String status,
        Boolean allDay
) {
}
