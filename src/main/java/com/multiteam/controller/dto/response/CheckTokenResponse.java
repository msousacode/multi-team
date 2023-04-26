package com.multiteam.controller.dto.response;

import java.util.UUID;

public record CheckTokenResponse(UUID userId, boolean isValid) {
}
