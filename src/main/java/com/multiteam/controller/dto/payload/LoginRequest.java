package com.multiteam.controller.dto.payload;

import com.multiteam.constants.ApplicationErrorsEnum;
import org.springframework.util.Assert;

public record LoginRequest(String email, String password) {

    public LoginRequest {
        Assert.isTrue(!email.isEmpty(), ApplicationErrorsEnum.VALUE_DOES_NOT_EMPTY.name());
        Assert.isTrue(!password.isEmpty(), ApplicationErrorsEnum.VALUE_DOES_NOT_EMPTY.name());
    }
}
