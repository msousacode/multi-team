package com.multiteam.service;

import com.multiteam.constants.TestsConstants;
import com.multiteam.controller.dto.GuestDto;
import com.multiteam.persistence.types.RelationshipType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class GuestServiceTest {

    @Autowired
    private GuestService guestService;

    @Test
    @DisplayName("deve criar um novo convidade então sucesso")
    void shouldCreateGuest_thenSuccess() {

        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        var guestDto = new GuestDto(
                null,
                "Danilo",
                "Joaquim",
                RelationshipType.PAI,
                "(82) 99424-8064",
                email,
                true,
                TestsConstants.PATIENT_ID
        );

        var result = guestService.createGuest(guestDto);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deve adicionar convidados ao tratamento com sucesso")
    void shouldAddGuestInTreatment_thenSuccess() {
        var result = guestService.addGuestInTreatment(TestsConstants.PATIENT_ID, TestsConstants.GUEST_ID);
        Assertions.assertTrue(result);
    }
}