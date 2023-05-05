package com.multiteam.service;

import com.multiteam.clinic.ClinicService;
import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.request.ClinicRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class ClinicServiceTest {

    @Autowired
    private ClinicService clinicService;

    @Test
    void shouldCreateClinicThenSuccess() {

        var name = UUID.randomUUID().toString().substring(0, 14) + "@Test";
        var cpfCnpj = UUID.randomUUID().toString().substring(0, 14);
        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        var request = new ClinicRequest(null, name, cpfCnpj, email, "99 988778877", "99 54540000", "", ConstantsToTests.OWNER_ID);

        var result = clinicService.createClinic(request);

        Assertions.assertTrue(result);
    }
/*
    @Test
    @DisplayName("deve retornar todas as clinicas")
    void shouldRetrieveAllClinics_thenSuccess() {
        var result = clinicService.getAllClinic();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("dado um clinicId que existe deve retornar a clinica consultada")
    void givenClinicIdThatExists_thenReturnClinicWithSuccess() {
        var result = clinicService.getClinicById(ConstantsToTests.CLINIC_ID);
        Assertions.assertEquals(result.get().getId(), ConstantsToTests.CLINIC_ID);
    }
 */
}