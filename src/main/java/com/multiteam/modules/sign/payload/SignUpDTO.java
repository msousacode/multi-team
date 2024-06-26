package com.multiteam.modules.sign.payload;

import com.multiteam.modules.role.Role;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public record SignUpDTO(String name, String email, String password, List<Role> roles) {

    public SignUpDTO {
        roles = new ArrayList<>();
        Assert.isTrue(!name.isEmpty(), "value name can`t be empty or null");
        Assert.isTrue(!email.isEmpty(), "value email can`t be empty or null");
        Assert.isTrue(!password.isEmpty(), "value password can`t be empty or null");
    }
}
