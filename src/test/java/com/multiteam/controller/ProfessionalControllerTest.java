package com.multiteam.controller;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.persistence.entity.User;
import com.multiteam.enums.RoleEnum;
import com.multiteam.enums.SpecialtyEnum;
import com.multiteam.persistence.repository.ProfessionalRepository;
import com.multiteam.persistence.repository.UserRepository;
import com.multiteam.service.ClinicService;
import com.multiteam.service.ProfessionalService;
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
public class ProfessionalControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessionalRepository professionalRepository;

    @MockBean
    private ProfessionalService professionalService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar um novo profissional então sucesso usando roles OWNER e ADMIN")
    void shouldCreateNewProfessional_thenSuccess() throws Exception {

        BDDMockito.given(professionalService.createProfessional(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER))
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
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve buscar o profissional usando roles OWNER e ADMIN então sucesso")
    void shouldGetProfessionalById_thenSuccess() throws Exception {

        BDDMockito.given(professionalService.getProfessional(Mockito.any())).willReturn(Optional.ofNullable(getProfessional()));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/professionals/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve atualizar o profissionais usando roles OWNER e ADMIN")
    void shouldUpdateProfessional_thenSuccess() throws Exception {

        BDDMockito.given(professionalService.updateProfessional(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER))
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve inativar o profissional usando roles OWNER e ADMIN")
    void shouldInactiveProfessional_thenSuccess() throws Exception {

        BDDMockito.given(professionalService.inactiveProfessional(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/professionals/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER))
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("deve tentar criar um novo profissional usando roles erradas então falha")
    void shouldCreateNewProfessionalWithRolesWrong_thenForbidden() throws Exception {

        BDDMockito.given(clinicService.getClinicById(Mockito.any())).willReturn(getClinic());
        BDDMockito.given(professionalRepository.save(new Professional())).willReturn(getProfessional());
        BDDMockito.given(userRepository.save(Mockito.any())).willReturn(new User());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve tentar buscar todos os profissionais usando roles erradas então falha")
    void shouldGetAllProfessionalsWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(professionalRepository.findAllByClinic_Id(Mockito.any())).willReturn(List.of());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/professionals/clinic/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve tentar atualizar o profissional usando roles erradas então falha")
    void shouldUpdateProfessionalWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(professionalService.updateProfessional(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/v1/professionals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .content(newProfessionalJson()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("deve tentar inativar o profissional usando roles erradas então falha")
    void shouldInactiveProfessionalWithRolesWrong_thenFail() throws Exception {

        BDDMockito.given(professionalService.inactiveProfessional(Mockito.any())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/v1/professionals/3a3bc57e-e4d3-44cd-a528-d528f2fc2a04")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_PROFESSIONAL))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
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
                SpecialtyEnum.FONOAUDIOLOGIA,
                "11 5656 - 0606",
                UUID.randomUUID().toString().substring(0, 10) + "@email.com",
                ConstantsToTests.CLINIC_ID
        );
    }

    Optional<Clinic> getClinic() {
        return Optional.ofNullable(
                new Clinic.Builder("Teste", "000000000000000", "teste@teste", "1199999-9999", new User()).build());
    }

    private Professional getProfessional() {
        return new Professional.Builder(
                UUID.randomUUID(),
                "Ana",
                "Analu",
                SpecialtyEnum.FONOAUDIOLOGIA,
                "(11) 98637-7492",
                "anaanaludasilva@fosjc.unesp.br",
                true, new Clinic(), new User())
                .build();
    }
}
