package com.multiteam.service;

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

        var patientId = UUID.fromString("7bdb248d-5f38-4060-8bb7-4a4f98a0ab52");
        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        var guestDto = new GuestDto(
                null,
                "Danilo",
                "Joaquim",
                RelationshipType.PAI,
                "(82) 99424-8064",
                email,
                true,
                patientId
        );

        var result = guestService.createGuest(guestDto);

        Assertions.assertTrue(result.success());
    }

    @Test
    @DisplayName("deve adicionar convidados ao tratamento com sucesso")
    void shouldAddGuestInTreatment_thenSuccess() {
        var guestId = UUID.fromString("29ce2c3f-4408-4123-9468-8a7811108bcf");
        var patientId = UUID.fromString("bc91b5cc-3bb8-48f4-abdf-8f16e33d6787");

        var result = guestService.addGuestInTreatment(patientId, guestId);
        Assertions.assertTrue(result);
    }
}