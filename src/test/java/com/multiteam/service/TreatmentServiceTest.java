package com.multiteam.service;

import com.multiteam.controller.dto.TreatmentDto;
import com.multiteam.persistence.types.SituationType;
import com.multiteam.persistence.types.TreatmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TreatmentServiceTest {

    @Autowired
    private TreatmentService treatamentService;

    @Test
    @DisplayName("deve criar o tratamento associado ao profissional")
    void shouldCreateTreatmentAssociatedWithProfessional_thenSuccess() {

        var clinicId = UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2");
        var professionalId = UUID.fromString("5adcab58-cbe3-42bf-b299-8c3d7682a3f9");
        var patientId = UUID.fromString("bc91b5cc-3bb8-48f4-abdf-8f16e33d6787");

        var treatmentDto = new TreatmentDto(
                null,
                "Lorem ipsum donec consectetur sagittis ullamcorper hac platea ultrices tristique, turpis curabitur commodo condimentum risus porttitor sit erat metus tempor, cras proin in odio eros ut dolor egestas. cras cursus elit sit urna mauris enim quisque eu class quam",
                TreatmentType.FONOAUDIOLOGIA,
                SituationType.ANDAMENTO,
                patientId,
                professionalId,
                clinicId,
                LocalDate.now());

        var result = treatamentService.includeTreatment(treatmentDto);

        assertTrue(result.success());
    }

    @Test
    @DisplayName("deve excluir tratamento com sucesso")
    void shouldExcludeTreatmentById_thenSuccess() {

        var treatmentId = UUID.fromString("df577d4e-235a-4d1d-b060-094fcd18e73c");

        var result = treatamentService.inactiveTreatment(treatmentId);

        Assertions.assertNull(result.content());
        Assertions.assertEquals(result.message(), "treatment deleted with success");
        Assertions.assertTrue(result.success());
    }

    @Test
    @DisplayName("deve obter todos os tratamentos que o convidado esta acompanhando")
    void shouldGetAllTreatments_thenSuccess() {

        var guestId = UUID.fromString("29ce2c3f-4408-4123-9468-8a7811108bcf");
        var result = treatamentService.getAllTreatmentsByGuestId(guestId);

        Assertions.assertFalse(result.isEmpty());
    }
}