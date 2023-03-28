package com.multiteam.service;

import com.multiteam.persistence.entity.Clinic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
class ClinicServiceTest {

    @Autowired
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar uma nova com sucesso")
    void shouldCreateClinic_thenSuccess() {

        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";
        var cpfCnpj = UUID.randomUUID().toString().substring(0, 14);

        var builder = new Clinic.Builder(
                "Geraldo Bryan Gomes",
                cpfCnpj,
                email,
                "(71) 3871-3197")
                .createdDate(LocalDateTime.now())
                .build();

        var result = clinicService.createClinic(builder);

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getEmail(), email);
        Assertions.assertEquals(result.getCpfCnpj(), cpfCnpj);
    }

    @Test
    @DisplayName("deve retornar todas as clinicas")
    void shouldRetrieveAllClinics_thenSuccess() {
        var result = clinicService.getAll();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("dado um clinicId que existe deve retornar a clinica consultada")
    void givenClinicIdThatExists_thenReturnClinicWithSuccess() {
        var clinicId = UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2");
        var result = clinicService.findById(clinicId);
        Assertions.assertEquals(result.get().getId(), clinicId);
    }

    @Test
    @DisplayName("dado um clinicId que não existe deve retornar vazio")
    void givenClinicIdThatNotExists_thenReturnEmpty() {
        var result = clinicService.findById(UUID.fromString("7bdb248d-5f38-4060-8bb7-4a4f98a0ab52"));
        Assertions.assertTrue(result.isEmpty());
    }
}