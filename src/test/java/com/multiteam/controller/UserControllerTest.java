package com.multiteam.controller;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.modules.user.UserDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestResponsePage;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest extends RestTemplateBase {

    @Test
    void shouldUpdatedPatientThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/users");

        var user = new UserDTO(
                UUID.fromString(ConstantsTest.USER_ID),
                UUID.randomUUID().toString(),
                UUID.randomUUID() + "@test.com",
                true,
                Set.of(ConstantsTest.ROLE_OWNER_ID));

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(user, headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Boolean.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

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
