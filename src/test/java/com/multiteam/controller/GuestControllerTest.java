package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.enums.RelationshipEnum;
import com.multiteam.enums.RoleEnum;
import com.multiteam.enums.SexEnum;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.service.GuestService;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AutoConfigureMockMvc
@SpringBootTest
public class GuestControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Test
    @DisplayName("deve criar um novo convidado então sucesso")
    void shouldCreateNewGuest_thenSuccess() throws Exception {

        BDDMockito.given(guestService.createGuest(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/guests")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_ADMIN))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(getNewGuest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("deve deletar um paciente então sucesso")
    void shouldDeleteGuest_thenSuccess() throws Exception {

        BDDMockito.given(guestService.deleteGuest(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/guests/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve atualizar o paciente usando permissão correta então sucesso")
    void shouldUpdateGuest_thenSuccess() throws Exception {

        BDDMockito.given(guestService.updateGuest(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/guests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .content(getNewGuest()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String getNewGuest() {
        return """
                {
                    "id":"%s",
                    "name":"%s",
                    "middleName":"%s",
                    "relationship":"%s",
                    "cellPhone":"%s",
                    "email":"%s",
                    "active":"%s",
                    "patientId":"%s"
                }
                """.formatted(
                UUID.randomUUID(),
                "Teste create guest",
                "Teste middle guest",
                RelationshipEnum.PAI,
                "12 9889-7878",
                "owguest@guest.com",
                true,
                ConstantsToTests.PATIENT_ID
        );
    }
}
