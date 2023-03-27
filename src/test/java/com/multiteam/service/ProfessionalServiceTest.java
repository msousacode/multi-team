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

    @Test
    @DisplayName("deve criar um novo profissonal e retornar sucesso")
    void shouldCreateNewProfessional_thenSuccess() {

        String name = UUID.randomUUID().toString().substring(0, 5);
        String middleName = UUID.randomUUID().toString().substring(0, 5);
        SpecialtyType specialty = SpecialtyType.FONOAUDIOLOGIA;
        String cellPhone = UUID.randomUUID().toString().substring(0, 5);
        String email = UUID.randomUUID().toString().substring(0, 5) + "@email.com";

        var professional = new Professional.Builder(null, name, middleName, specialty, cellPhone, email, true, null).build();

        var result = professionalService.createProfessional(professional);

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