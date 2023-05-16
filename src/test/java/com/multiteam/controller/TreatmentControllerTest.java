package com.multiteam.controller;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.treatment.dto.TreatmentRequest;
import com.multiteam.modules.treatment.dto.TreatmentResponse;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreatmentControllerTest extends RestTemplateBase {

    @Test
    void shouldCreateNewTreatmentThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/treatments");

        var treatment = new TreatmentRequest(
                null,
                "Lorem ipsum volutpat taciti tempor porttitor ultricies vitae, venenatis aenean laoreet nulla nibh dictumst auctor gravida, phasellus platea varius ultricies venenatis etiam. lacinia lorem dapibus nam donec fermentum vitae nec sem vivamus sociosqu, consequat ",
                SituationEnum.ANDAMENTO,
                UUID.fromString(ConstantsTest.PATIENT_ID),
                UUID.fromString(ConstantsTest.PROFESSIONAL_ID),
                UUID.fromString(ConstantsTest.CLINIC_ID),
                LocalDate.now(),
                null);

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL));
        HttpEntity<Object> request = new HttpEntity<>(treatment, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldUpdatedTreatmentThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/treatments");

        var treatment = new TreatmentRequest(
                UUID.fromString(ConstantsTest.TREATMENT_ID),
                "Lorem ipsum volutpat taciti tempor porttitor ultricies vitae.",
                SituationEnum.ANDAMENTO,
                UUID.fromString(ConstantsTest.PATIENT_ID),
                UUID.fromString(ConstantsTest.PROFESSIONAL_ID),
                UUID.fromString(ConstantsTest.CLINIC_ID),
                LocalDate.now(),
                LocalDate.now());

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(treatment, headers);

        ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetTreatmentByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/treatments/" + ConstantsTest.TREATMENT_ID);

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<TreatmentResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, TreatmentResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
