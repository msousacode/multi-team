package com.multiteam.service;

import com.multiteam.persistence.entity.Credential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CredentialServiceTest {

    @Autowired
    private CredentialService credentialService;

    @Test
    @DisplayName("deve criar uma nova credencial, então retornará sucesso.")
    void shouldCreateNewCredential_thenSuccess() {

        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";
        var password = "12345678";

        var credential = new Credential(email, password);

        var result = credentialService.createCredential(credential);

        assertTrue(Objects.nonNull(result.getId()));
        assertEquals(result.getUsername(), email);
    }

    @Test
    @DisplayName("deve retornar falha quando tentar criar nova credencidal sem o username.")
    void whenToTryCreateCredentialWithoutUsername_thenIllegalArgumentException() {
        var email = "";
        var password = "12345678";
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Credential(email, password));
    }
}