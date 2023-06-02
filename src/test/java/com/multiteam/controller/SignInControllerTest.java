package com.multiteam.controller;

import com.multiteam.modules.signin.payload.SignInDTO;
import com.multiteam.modules.signin.payload.SignUpDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SignInControllerTest extends RestTemplateBase {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    void shouldPerformSingUpThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/sign-up");

        var signUp = new SignUpDTO(UUID.randomUUID().toString().substring(0,10), UUID.randomUUID().toString().substring(0,10) + "@test.com", "12345678", List.of());

        HttpEntity<Object> request = new HttpEntity<>(signUp, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldPerformSingInThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/sign-in");

        var signIn = new SignInDTO(ConstantsTest.EMAIL_OWNER, "12345678");

        HttpEntity<Object> request = new HttpEntity<>(signIn, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}