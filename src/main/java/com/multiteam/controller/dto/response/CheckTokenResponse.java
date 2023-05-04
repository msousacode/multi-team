package com.multiteam.controller.dto.response;

public record CheckTokenResponse(String userId, String ownerId, boolean isValid) {
}
