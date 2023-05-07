package com.multiteam.controller;

import com.multiteam.clinic.Clinic;
import com.multiteam.clinic.ClinicDTO;
import com.multiteam.util.ConstantsTests;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClinicControllerTest extends TokenUtil {

    @Test
    void shouldCreateNewClinicThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var clinic = new Clinic.Builder(
                UUID.randomUUID() + " @Test",
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID().toString().substring(0, 15),
                UUID.randomUUID().toString().substring(0, 15))
                .build();

        HttpEntity<Object> request = new HttpEntity<>(clinic, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldUpdatedClinicThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var clinic = new Clinic.Builder(
                UUID.randomUUID() + " @Test",
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID().toString().substring(0, 15),
                UUID.randomUUID().toString().substring(0, 15))
                .id(UUID.fromString(ConstantsTests.CLINIC_ID))
                .build();

        HttpEntity<Object> request = new HttpEntity<>(clinic, headers);

        ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ClinicDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetAllClinicsThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<List<Clinic>> response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
        });

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetClinicByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics/" + ConstantsTests.CLINIC_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<ClinicDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, ClinicDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
