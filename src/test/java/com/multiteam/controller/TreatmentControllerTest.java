package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.request.TreatmentRequest;
import com.multiteam.enums.RoleEnum;
import com.multiteam.enums.SituationEnum;
import com.multiteam.enums.TreatmentEnum;
import com.multiteam.service.TreatmentService;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest
public class TreatmentControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TreatmentService treatmentService;

    @Test
    @DisplayName("deve criar um novo tratamento")
    void shouldCreateNewTreatment_thenSuccess() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/treatments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_ADMIN))
                                .content(getNewTreatment()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("deve criar um novo tratamento utilizando a permissão de escrita então sucesso")
    void shouldCreateNewTreatmentUsingWritePermissions_thenSuccess() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/treatments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_WRITE))
                                .content(getNewTreatment()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("deve tentar criar um novo tratamento utilizando uma role inválida então falha")
    void shouldTryCreateNewTreatmentUsingInvalidPermissions_thenFail() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/treatments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .content(getNewTreatment()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    String getNewTreatment() {
        return """
                {
                    "description":"%s",
                    "treatmentType":"%s",
                    "situation":"%s",
                    "patientId":"%s",
                    "professionalId":"%s",
                    "clinicId":"%s",
                    "initialDate":"%s",
                    "finalDate":"%s"
                }
                """
                .formatted(
                        "Lorem ipsum dolor tincidunt etiam donec, mauris semper quisque aptent, tincidunt cubilia habitasse fames.",
                        TreatmentEnum.FONOAUDIOLOGIA,
                        SituationEnum.ANDAMENTO,
                        ConstantsToTests.PATIENT_ID,
                        ConstantsToTests.PROFESSIONAL_ID,
                        ConstantsToTests.CLINIC_ID,
                        LocalDate.now(),
                        LocalDate.now());
    }
}
