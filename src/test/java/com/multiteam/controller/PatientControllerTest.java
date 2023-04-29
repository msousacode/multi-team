package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.enums.RoleEnum;
import com.multiteam.enums.SexEnum;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.service.PatientService;
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
public class PatientControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    @DisplayName("deve criar um novo paciente então sucesso")
    void shouldCreateNewPatient_thenSuccess() throws Exception {

        BDDMockito.given(patientService.createPatient(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/patients")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_ADMIN))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(getNewPatient()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("deve buscar o paciente por patientId e clinicId então sucesso")
    void shouldGetPatientById_thenSuccess() throws Exception {

        BDDMockito.given(patientService.getPatientById(Mockito.any(), Mockito.any())).willReturn(Optional.of(new Patient()));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/patients/clinic/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04/patient/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_PATIENT_READ)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve buscar todos os pacientes então sucesso")
    void shouldGetAllPatients_thenSuccess() throws Exception {

        BDDMockito.given(patientService.getAllPatientsByClinicId(Mockito.any(), Mockito.any())).willReturn(List.of());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/patients/clinic/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_PATIENT_READ)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve inativar um paciente então sucesso")
    void shouldInactivePatient_thenSuccess() throws Exception {
        BDDMockito.given(patientService.inactivePatient(Mockito.any(), Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/patients/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04/clinic/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_PATIENT_WRITE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve atualizar o paciente usando permissão correta então sucesso")
    void shouldUpdatePatient_thenSuccess() throws Exception {

        BDDMockito.given(patientService.updatePatient(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.PERM_PATIENT_WRITE))
                                .content(getNewPatient()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String getNewPatient() {
        return """
                {
                    "id":"%s",
                    "name":"%s",                    
                    "sex":"%s",
                    "age":"%s",
                    "months":"%s",                    
                    "active":"%s",
                    "ownerId":"%s"
                }
                """.formatted(
                UUID.randomUUID(),
                "Teste create patient",
                "Teste middle create",
                SexEnum.MASCULINO,
                12,
                2,
                "Sed aliquam phasellus pharetra rhoncus dolor ultricies vestibulum himenaeos sodales.",
                "Sed aliquam phasellus pharetra rhoncus dolor ultricies vestibulum himenaeos sodales.",
                true,
                ConstantsToTests.CLINIC_ID
        );
    }
}
