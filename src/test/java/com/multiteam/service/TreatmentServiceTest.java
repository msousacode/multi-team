package com.multiteam.service;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.request.TreatmentRequest;
import com.multiteam.enums.SituationEnum;
import com.multiteam.enums.TreatmentEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TreatmentServiceTest {

    @Autowired
    private TreatmentService treatamentService;

    @Test
    @DisplayName("deve criar o tratamento associado ao profissional")
    void shouldCreateTreatmentAssociatedWithProfessional_thenSuccess() {

        var treatmentDto = new TreatmentRequest(
                null,
                "Lorem ipsum donec consectetur sagittis ullamcorper hac platea ultrices tristique, turpis curabitur commodo condimentum risus porttitor sit erat metus tempor, cras proin in odio eros ut dolor egestas. cras cursus elit sit urna mauris enim quisque eu class quam",
                TreatmentEnum.FONOAUDIOLOGIA,
                SituationEnum.ANDAMENTO,
                ConstantsToTests.PATIENT_ID,
                ConstantsToTests.PROFESSIONAL_ID,
                ConstantsToTests.CLINIC_ID,
                LocalDate.now(),
                null);

        var result = treatamentService.includeTreatment(treatmentDto);

        assertTrue(result);
    }
/*
    @Test
    @DisplayName("deve excluir tratamento com sucesso")
    void shouldExcludeTreatmentById_thenSuccess() {

        var result = treatamentService.inactiveTreatment(TestsConstants.TREATMENT_ID);

        Assertions.assertNull(result.content());
        Assertions.assertEquals(result.message(), "treatment deleted with success");
        Assertions.assertTrue(result.success());
    }
*/
    @Test
    @DisplayName("deve obter todos os tratamentos que o convidado esta acompanhando")
    void shouldGetAllTreatments_thenSuccess() {
        var result = treatamentService.getAllTreatmentsByGuestId(ConstantsToTests.GUEST_ID);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve obter o tratamento por treatmentId")
    void shouldGetTreatmentById_thenSuccess() {
        var result = treatamentService.getTreatmentById(ConstantsToTests.TREATMENT_ID);
        Assertions.assertNotNull(result.get());
    }
}