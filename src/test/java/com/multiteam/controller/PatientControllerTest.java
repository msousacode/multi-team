package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.enums.RoleEnum;
import com.multiteam.enums.SexEnum;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.service.PatientService;
import com.multiteam.util.TokenUtil;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@AutoConfigureMockMvc
@SpringBootTest
public class PatientControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
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
    void shouldGetAllPatients_thenSuccess() {

    }

    @Test
    void shouldInactivePatient_thenSuccess() {

    }

    private String getNewPatient() {
        return """
                {
                    "id":"%s",
                    "name":"%s",
                    "middleName":"%s",
                    "sex":"%s",
                    "age":"%s",
                    "months":"%s",
                    "internalObservation":"%s",
                    "externalObservation":"%s",
                    "active":"%s",
                    "clinicId":"%s"
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
