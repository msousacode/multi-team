package com.multiteam.auth.dto;

import org.springframework.util.Assert;

public record LoginRequest(String email, String password) {

    public LoginRequest {
        Assert.isTrue(!email.isEmpty(), "value email can`t be empty or null");
        Assert.isTrue(!password.isEmpty(), "value password can`t be empty or null");
    }
}
