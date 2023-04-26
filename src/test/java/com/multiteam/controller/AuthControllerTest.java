package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockMvcController {

    @Test
    @DisplayName("deve criar um novo usuário sem roles então sucesso")
    public void shouldRegisterNewUserWithOutRoles_thenSuccess() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/signup")
                                .content(getNewUserWithoutRoleJson())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("deve criar um novo usuário com roles então sucesso")
    public void shouldRegisterNewUserWithRoles_thenSuccess() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/signup")
                                .content(getNewUserWithRoleJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("deve realizar a autenticação do usuário então sucesso")
    public void shouldPerformAuthenticationUser_thenGeneratedTokenWithHttpStatus200() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/login")
                                .content(getUserJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    private String getNewUserWithRoleJson() {
        return """
                {
                    "name":"%s",
                    "email":"%s",
                    "password":"12345678",
                    "roles": [
                        {
                            "id": "03dc776a-3bb2-45f6-b186-a0d0a7c16894",
                            "role": "ROLE_ADMIN"
                        },
                        {
                            "id": "6252fbab-2c24-48e3-bd3b-bd36cf9faebf",
                            "role": "ROLE_PROFESSIONAL"
                        }
                    ]
                }
                """.formatted(
                        "Ow Test+" + UUID.randomUUID().toString().substring(0,5),
                        UUID.randomUUID().toString().substring(0,5) + "@email.com");
    }

    private String getUserJson() {
        return """
                {
                    "email":"%s",
                    "password":"12345678"
                }
                """.formatted(ConstantsToTests.USER_OWNER_ADMIN);
    }

    private String getNewUserWithoutRoleJson() {
        return """
                {
                    "name":"%s",
                    "email":"%s",
                    "password":"12345678"
                }
                """.formatted(
                        "Ow Test+" + UUID.randomUUID().toString().substring(0,5),
                        UUID.randomUUID().toString().substring(0,5) + "@email.com");
    }
}