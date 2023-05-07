package com.multiteam.controller;

import com.multiteam.clinic.Clinic;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.professional.ProfessionalDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessionalControllerTest extends RestTemplateBase {

    @Test
    void shouldCreateNewProfessionalThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var professional = new ProfessionalDTO(
                null,
                UUID.randomUUID().toString().substring(0, 14),
                SpecialtyEnum.FONOAUDIOLOGIA.getName(),
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID() + "@email.com",
                Set.of(ConstantsTest.CLINIC_ID));

        HttpEntity<Object> request = new HttpEntity<>(professional, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldUpdatedProfessionalThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var professional = new ProfessionalDTO(
                UUID.fromString(ConstantsTest.PROFESSIONAL_ID),
                UUID.randomUUID().toString().substring(0, 14),
                SpecialtyEnum.FONOAUDIOLOGIA.getName(),
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID() + "@email.com",
                Set.of(ConstantsTest.CLINIC_ID));

        HttpEntity<Object> request = new HttpEntity<>(professional, headers);

        ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ProfessionalDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetAllProfessionalsThenSuccess() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<List<Clinic>> response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
        });
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetProfessionalByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals/" + ConstantsTest.PROFESSIONAL_ID);

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<ProfessionalDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, ProfessionalDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
