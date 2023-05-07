package com.multiteam.controller;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.patient.PatientDTO;
import com.multiteam.professional.ProfessionalDTO;
import com.multiteam.user.UserDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestResponsePage;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest extends RestTemplateBase {

    @Test
    void shouldGetAllUsersThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/users");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<UserDTO>> responseType = new ParameterizedTypeReference<>() { };
        ResponseEntity<RestResponsePage<UserDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, request, responseType);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetUserByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/users/" + ConstantsTest.USER_ID);

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, UserDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(jsonPath("id"));
    }
}
