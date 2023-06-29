package com.multiteam.modules.schedule;

import com.multiteam.modules.patient.dto.PatientDTO;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ScheduleRequest(
        UUID id,
        PatientDTO patient,
        UUID clinicId,
        UUID professionalId,
        LocalDateTime start,
        LocalDateTime end,
        String description,
        Boolean active,
        String status,
        Boolean repeatEvent,
        List<Integer> dayOfWeek
) {
}
