package com.multiteam.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockMvcController {

    @Test
    @DisplayName("deve realizar a autenticação do usuário então sucesso")
    public void shouldPerformAuthenticationUser_thenGeneratedTokenWithHttpStatus200() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/login")
                                .content(getUserJson())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("deve criar um novo usuário então sucesso")
    public void shouldRegisterNewUser_thenSuccess() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/signup")
                                .content(getUserNewJson())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    private String getUserJson() {
        return """
                {
                    "email":"redtest@email.com",
                    "password":"12345678"
                }
                """;
    }

    private String getUserNewJson() {
        return """
                {
                    "name":"RedTest",
                    "email":"redtest@email.com",
                    "password":"12345678"
                }
                """;
    }
}