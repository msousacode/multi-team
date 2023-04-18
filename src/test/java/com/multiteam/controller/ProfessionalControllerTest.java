package com.multiteam.controller;

import com.multiteam.constants.TestsConstants;
import com.multiteam.persistence.types.SpecialtyType;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@AutoConfigureMockMvc
@SpringBootTest
public class ProfessionalControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Test
    void shouldCreateNewProfessional_thenSuccess() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + defaultAccessToken)
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private String newProfessionalJson() {
        return """
                {
                    "name":"%s",
                    "middleName":"%s",
                    "specialty":"%s",
                    "cellPhone":"%s",
                    "email":"%s",
                    "clinicId":"%s"
                }
                """.formatted(
                UUID.randomUUID().toString().substring(0, 10),
                UUID.randomUUID().toString().substring(0, 10),
                SpecialtyType.FONOAUDIOLOGIA,
                "11 5656 - 0606",
                UUID.randomUUID().toString().substring(0, 10) + "@email.com",
                TestsConstants.CLINIC_ID
        );
    }
}
