package com.multiteam.controller;

import com.multiteam.modules.anamnese.dto.AnamneseRequest;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnamneseController extends RestTemplateBase {

    @Test
    void shouldCreateNewAnamneseThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/anamneses");

        var clinic = new AnamneseRequest(
                null,
                "Lorem ipsum volutpat taciti tempor porttitor ultricies vitae, venenatis aenean laoreet nulla nibh dictumst auctor gravida, phasellus platea varius ultricies venenatis etiam. lacinia lorem dapibus nam donec fermentum vitae nec sem vivamus sociosqu, consequat ",
                "Lorem ipsum volutpat taciti tempor porttitor ultricies vitae, venenatis aenean laoreet nulla nibh dictumst auctor gravida, phasellus platea varius ultricies venenatis etiam. lacinia lorem dapibus nam donec fermentum vitae nec sem vivamus sociosqu, consequat ",
                AnamneseEnum.OPEN,
                UUID.fromString(ConstantsTest.PATIENT_ID),
                true
        );

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(clinic, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }
}
