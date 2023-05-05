package com.multiteam.auth.dto;

public record CheckTokenResponse(String userId, String ownerId, boolean isValid) {
}
