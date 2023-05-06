package com.multiteam.signin.dto;

public record CheckTokenResponse(String userId, String ownerId, boolean isValid) {
}
