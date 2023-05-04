package com.multiteam.service;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.ProfessionalDTO;
import com.multiteam.enums.SpecialtyEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
class ProfessionalServiceTest {

    @Autowired
    private ProfessionalService professionalService;

    @Autowired
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar um novo profissonal e retornar sucesso")
    void shouldCreateNewProfessional_thenSuccess() {

        var name = UUID.randomUUID().toString().substring(0, 5);
        var middleName = UUID.randomUUID().toString().substring(0, 5);
        var cellPhone = UUID.randomUUID().toString().substring(0, 5);
        var email = "Ow Test+" + UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        Set<String> clinics = new HashSet<>();
        clinics.add(ConstantsToTests.CLINIC_ID.toString());

        var professional = new ProfessionalDTO(null, name, middleName, SpecialtyEnum.FONOAUDIOLOGIA.getName(), cellPhone, email, clinics);

        var result = professionalService.createProfessional(professional);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deve buscar a lista dos profissionais cadastrados com sucesso")
    void shouldRetrieveProfessionalList_thenSuccess() {
        var professionals = professionalService.getAllProfessionals(ConstantsToTests.CLINIC_ID);
        Assertions.assertFalse(professionals.isEmpty());
    }

    @Test
    @DisplayName("dado um professionalId que existe deve retornar o profissional consultado")
    void givenClinicIdThatExists_thenReturnClinicWithSuccess() {
        var professionalId = ConstantsToTests.PROFESSIONAL_ID;
        var result = professionalService.getProfessionalById(professionalId);
        Assertions.assertEquals(result.get().getId(), professionalId);
    }
/*
    @Test
    @DisplayName("deverá inativar o profissional então sucesso")
    void shouldInactivateTheProfessional_thenSuccess() {
        var result = professionalService.professionalInactive(TestsConstants.PROFESSIONAL_ID);
        Assertions.assertTrue(result.success());
    }
 */
}