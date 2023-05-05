package com.multiteam.controller;

import com.multiteam.clinic.Clinic;
import com.multiteam.constants.ConstantsToTests;
import com.multiteam.core.enums.RelationshipEnum;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.service.GuestService;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClinicControllerTest extends TokenUtil {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void shouldCreateNewClinicThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
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
}
