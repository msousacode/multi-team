package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.persistence.projection.TreatmentView;
import com.multiteam.service.TreatmentService;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class TreatmentControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreatmentService treatmentService;

    @Test
    @DisplayName("deve criar um novo tratamento")
    void shouldCreateNewTreatment_thenSuccess() throws Exception {

        BDDMockito.given(treatmentService.includeTreatment(Mockito.any())).willReturn(Boolean.TRUE);

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

        BDDMockito.given(treatmentService.includeTreatment(Mockito.any())).willReturn(Boolean.TRUE);

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

    @Test
    @DisplayName("deve inativar o tratamento usando permissão correta então sucesso")
    void shouldInactiveTreatment_thenSuccess() throws Exception {

        BDDMockito.given(treatmentService.inactiveTreatment(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/treatments/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_WRITE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve tentar inativar o tratamento usando roles inválidas então falha")
    void shouldInactiveTreatmentWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(treatmentService.inactiveTreatment(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/treatments/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve atualizar o tratamento usando permissão correta então sucesso")
    void shouldUpdateTreatment_thenSuccess() throws Exception {

        BDDMockito.given(treatmentService.updateTreatment(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/treatments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_WRITE))
                                .content(getNewTreatment()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve tentar atualizar o tratamento usando permissão errada então falha")
    void shouldUpdateTreatmentWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(treatmentService.updateTreatment(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/treatments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .content(getNewTreatment()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve buscar todos os tratamentos usando permissão válida então sucesso")
    void shouldGetAllTreatments_thenSuccess() throws Exception {

        BDDMockito.given(treatmentService.getAllTreatmentsByPatientId(Mockito.any())).willReturn((Set.of()));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/treatments/patient/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_READ)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve tentar buscar todos os tratamentos usando permissão inválida então falha")
    void shouldGetAllTreatmentsWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(treatmentService.getAllTreatmentsByPatientId(Mockito.any())).willReturn((Set.of()));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/treatments/patient/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_WRITE)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve buscar o tratamento por treatementId usando permissão correta então sucesso")
    void shouldGetTreatmentById_thenSuccess() throws Exception {

        BDDMockito.given(treatmentService.getTreatmentById(Mockito.any())).willReturn(getTreatmentView());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/treatments/" + ConstantsToTests.TREATMENT_ID)
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_TREATMENT_READ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
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

    private static Optional<TreatmentView> getTreatmentView() {
        return Optional.of(new TreatmentView() {
            @Override
            public UUID getId() {
                return null;
            }

            @Override
            public TreatmentEnum getTreatmentType() {
                return null;
            }

            @Override
            public SituationEnum getSituation() {
                return null;
            }

            @Override
            public String getPatientName() {
                return null;
            }

            @Override
            public String getProfessionalName() {
                return null;
            }

            @Override
            public String getProfessionalMiddleName() {
                return null;
            }

            @Override
            public LocalDate getInitialDate() {
                return null;
            }

            @Override
            public LocalDate getFinalDate() {
                return null;
            }

            @Override
            public Boolean isActive() {
                return null;
            }
        });
    }
}
