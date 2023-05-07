package com.multiteam.controller;

import com.multiteam.clinic.Clinic;
import com.multiteam.clinic.ClinicDTO;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.SexEnum;
import com.multiteam.patient.Patient;
import com.multiteam.patient.PatientDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestResponsePage;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientControllerTest extends RestTemplateBase {

    @Test
    void shouldCreateNewPatientThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var patient = new PatientDTO(
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID() + "@test.com",
                UUID.randomUUID().toString().substring(0, 14),
                SexEnum.NAO_DECLARADO.getDescription(),
                20,
                LocalDate.now());

        HttpEntity<Object> request = new HttpEntity<>(patient, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldUpdatedPatientThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var patient = new PatientDTO(
                UUID.fromString(ConstantsTest.PATIENT_ID),
                UUID.randomUUID().toString(),
                UUID.randomUUID() + "@test.com",
                UUID.randomUUID().toString().substring(0, 14),
                SexEnum.NAO_DECLARADO.getDescription(),
                20,
                LocalDate.now());

        HttpEntity<Object> request = new HttpEntity<>(patient, headers);

        ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ClinicDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetAllPatientsThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<PatientDTO>> responseType = new ParameterizedTypeReference<>() { };
        ResponseEntity<RestResponsePage<PatientDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, request, responseType);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetClinicByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics/" + ConstantsTest.CLINIC_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<ClinicDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, ClinicDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
