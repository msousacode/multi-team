package com.multiteam.controller.dto.payload;

import com.multiteam.constants.ApplicationErrorsEnum;
import com.multiteam.persistence.entity.Role;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

public record SignUpRequest(String name, String email, String password, Set<Role> roles) {

    public SignUpRequest{
        roles = new HashSet<>();
        Assert.isTrue(!name.isEmpty(), ApplicationErrorsEnum.VALUE_DOES_NOT_EMPTY.name());
        Assert.isTrue(!email.isEmpty(), ApplicationErrorsEnum.VALUE_DOES_NOT_EMPTY.name());
        Assert.isTrue(!password.isEmpty(), ApplicationErrorsEnum.VALUE_DOES_NOT_EMPTY.name());
    }
}
