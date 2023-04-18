package com.multiteam.controller;

import com.multiteam.constants.TestsConstants;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.persistence.repository.UserRepository;
import com.multiteam.persistence.enums.SpecialtyType;
import com.multiteam.service.ClinicService;
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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
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

    @MockBean
    private ProfessionalRepository professionalRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar um novo profissional então sucesso usando roles OWNER e ADMIN")
    void shouldCreateNewProfessional_thenSuccess() throws Exception {

        BDDMockito.given(clinicService.getClinicById(Mockito.any())).willReturn(getClinic());
        BDDMockito.given(professionalRepository.save(new Professional())).willReturn(getProfessional());
        BDDMockito.given(userRepository.save(Mockito.any())).willReturn(new User());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + defaultAccessToken)
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("deve buscar todos os profissionais usando roles OWNER e ADMIN")
    void shouldGetAllProfessionals_thenSuccess() throws Exception {

        BDDMockito.given(professionalRepository.findAllByClinic_Id(Mockito.any())).willReturn(List.of());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/professionals/clinic/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + defaultAccessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
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

    Optional<Clinic> getClinic() {
        return Optional.ofNullable(
                new Clinic.Builder("Teste", "000000000000000", "teste@teste", "1199999-9999").build());
    }

    private Professional getProfessional() {
        return new Professional.Builder(
                UUID.randomUUID(),
                "Ana",
                "Analu",
                SpecialtyType.FONOAUDIOLOGIA,
                "(11) 98637-7492",
                "anaanaludasilva@fosjc.unesp.br",
                true, new Clinic(), new User())
                .build();
    }
}
