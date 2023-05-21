package com.multiteam.modules.schedule;

import java.util.Calendar;
import java.util.UUID;

public record ScheduleRequest(
        UUID patientId,
        UUID clientId,
        UUID professionalId,
        String title,
        Calendar start,
        Calendar end,
        String url,
        String description,
        String color,
        Boolean active
) {
}
