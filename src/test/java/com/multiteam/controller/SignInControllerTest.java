package com.multiteam.controller;

import com.multiteam.util.Constants;
import com.multiteam.signin.dto.SignInDTO;
import com.multiteam.signin.dto.SignUpDTO;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignInControllerTest extends TokenUtil {

    @Test
    void shouldPerformSingUpThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/sign-up");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var signUp = new SignUpDTO(UUID.randomUUID().toString().substring(0,10), UUID.randomUUID().toString().substring(0,10) + "@test.com", "12345678", Set.of());

        HttpEntity<Object> request = new HttpEntity<>(signUp, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldPerformSingInThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/sign-in");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var signIn = new SignInDTO(Constants.USER_OWNER_ADMIN, "12345678");

        HttpEntity<Object> request = new HttpEntity<>(signIn, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}