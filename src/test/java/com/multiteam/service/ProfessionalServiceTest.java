package com.multiteam.service;

import com.multiteam.constants.TestsConstants;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.types.SpecialtyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        var clinicDefault = TestsConstants.CLINIC_ID;

        var name = UUID.randomUUID().toString().substring(0, 5);
        var middleName = UUID.randomUUID().toString().substring(0, 5);
        var specialty = SpecialtyType.FONOAUDIOLOGIA;
        var cellPhone = UUID.randomUUID().toString().substring(0, 5);
        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";
        var clinic = clinicService.getClinicById(clinicDefault);

        var professional = new Professional.Builder(null, name, middleName, specialty, cellPhone, email, true, clinic.get()).build();

        var result = professionalService.createProfessional(professional, clinicDefault);

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getName(), name);
        Assertions.assertEquals(result.getMiddleName(), middleName);
        Assertions.assertEquals(result.getCellPhone(), cellPhone);
        Assertions.assertEquals(result.getEmail(), email);
    }

    @Test
    @DisplayName("deve buscar a lista dos profissionais cadastrados com sucesso")
    void shouldRetrieveProfessionalList_thenSuccess() {
        var professionals = professionalService.getAllProfessionals(TestsConstants.CLINIC_ID);
        Assertions.assertFalse(professionals.isEmpty());
    }

    @Test
    @DisplayName("dado um professionalId que existe deve retornar o profissional consultado")
    void givenClinicIdThatExists_thenReturnClinicWithSuccess() {
        var professionalId = TestsConstants.PROFESSIONAL_ID;
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