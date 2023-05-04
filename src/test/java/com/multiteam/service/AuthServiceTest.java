package com.multiteam.service;

import com.multiteam.constants.ConstantsToTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void shouldValidTokenAndGetInfoUserLogged_thenSuccess() {
        var response = authService.checkToken(ConstantsToTests.VALID_TOKEN);
        Assertions.assertTrue(response.isValid());
    }
}
