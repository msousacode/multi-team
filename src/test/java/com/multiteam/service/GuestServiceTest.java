package com.multiteam.service;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.request.GuestRequest;
import com.multiteam.persistence.enums.RelationshipType;
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
    @DisplayName("deve criar um novo convidado então sucesso")
    void shouldCreateGuest_thenSuccess() {

        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        var guestDto = new GuestRequest(
                null,
                "Danilo",
                "Joaquim",
                RelationshipType.PAI,
                "(82) 99424-8064",
                email,
                true,
                ConstantsToTests.PATIENT_ID
        );

        var result = guestService.createGuest(guestDto);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deve adicionar convidados ao tratamento com sucesso")
    void shouldAddGuestInTreatment_thenSuccess() {
        var result = guestService.addGuestInTreatment(ConstantsToTests.PATIENT_ID, ConstantsToTests.GUEST_ID);
        Assertions.assertTrue(result);
    }
}