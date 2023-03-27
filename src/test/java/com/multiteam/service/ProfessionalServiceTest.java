package com.multiteam.service;

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

        var clinicDefault = UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2");

        var name = UUID.randomUUID().toString().substring(0, 5);
        var middleName = UUID.randomUUID().toString().substring(0, 5);
        var specialty = SpecialtyType.FONOAUDIOLOGIA;
        var cellPhone = UUID.randomUUID().toString().substring(0, 5);
        var email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";
        var clinic = clinicService.findById(clinicDefault);

        var professional = new Professional.Builder(null, name, middleName, specialty, cellPhone, email, true, clinic).build();

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
        var professionals = professionalService.getProfessionals();
        Assertions.assertFalse(professionals.isEmpty());
    }
}