package com.multiteam.service;

import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateNewUserThenSuccess() {

        var name = UUID.randomUUID().toString() + "@Test";
        var email = UUID.randomUUID().toString() + "@email.com";

        var user = userService.createUser(name, email, AuthProviderEnum.local);

        Assertions.assertNotNull(user.getId());
    }
}
