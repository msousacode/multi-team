package com.multiteam.modules.document.dto;

import java.util.UUID;

public record DocumentSearch(
    UUID treatmentId,
    UUID patientId
) {
}
