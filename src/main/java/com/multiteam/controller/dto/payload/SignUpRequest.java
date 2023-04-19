package com.multiteam.controller.dto.payload;

import com.multiteam.constants.ApplicationErrorsEnum;
import com.multiteam.persistence.entity.Role;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

public record SignUpRequest(String name, String email, String password, Set<Role> roles) {

    public SignUpRequest{
        roles = new HashSet<>();
        Assert.isTrue(!name.isEmpty(), "value name can`t be empty or null");
        Assert.isTrue(!email.isEmpty(), "value email can`t be empty or null");
        Assert.isTrue(!password.isEmpty(), "value password can`t be empty or null");
    }
}
