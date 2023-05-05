package com.multiteam.service;

import com.multiteam.clinic.ClinicService;
import com.multiteam.constants.ConstantsToTests;
import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.professional.ProfessionalDTO;
import com.multiteam.professional.ProfessionalService;
import org.junit.jupiter.api.Assertions;
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
    void shouldCreateNewProfessionalThenSuccess() {

        var name = UUID.randomUUID().toString().substring(0, 5);
        var middleName = UUID.randomUUID().toString().substring(0, 5);
        var cellPhone = UUID.randomUUID().toString().substring(0, 5);
        var email = "Ow Test+" + UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        Set<String> clinics = new HashSet<>();
        clinics.add(ConstantsToTests.CLINIC_ID.toString());

        var professional = new ProfessionalDTO(null, name, SpecialtyEnum.FONOAUDIOLOGIA.getName(), cellPhone, email, clinics);

        var result = professionalService.createProfessional(professional);

        Assertions.assertTrue(result);
    }
/*
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