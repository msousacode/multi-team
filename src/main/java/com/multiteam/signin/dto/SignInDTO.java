package com.multiteam.signin.dto;

import org.springframework.util.Assert;

public record SignInDTO(String email, String password) {

    public SignInDTO {
        Assert.isTrue(!email.isEmpty(), "value email can`t be empty or null");
        Assert.isTrue(!password.isEmpty(), "value password can`t be empty or null");
    }
}
