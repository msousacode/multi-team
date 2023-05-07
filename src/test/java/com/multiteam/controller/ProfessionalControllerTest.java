package com.multiteam.controller;

import com.multiteam.clinic.Clinic;
import com.multiteam.clinic.ClinicDTO;
import com.multiteam.core.enums.RoleEnum;
import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.professional.Professional;
import com.multiteam.professional.ProfessionalDTO;
import com.multiteam.professional.ProfessionalRepository;
import com.multiteam.user.UserRepository;
import com.multiteam.clinic.ClinicService;
import com.multiteam.professional.ProfessionalService;
import com.multiteam.util.ConstantsTests;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessionalControllerTest extends TokenUtil {

    @Test
    void shouldCreateNewProfessionalThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var professional = new ProfessionalDTO(
                null,
                UUID.randomUUID().toString().substring(0, 14),
                SpecialtyEnum.FONOAUDIOLOGIA.getName(),
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID() + "@email.com",
                Set.of(ConstantsTests.CLINIC_ID));

        HttpEntity<Object> request = new HttpEntity<>(professional, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldUpdatedProfessionalThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var professional = new ProfessionalDTO(
                UUID.fromString(ConstantsTests.PROFESSIONAL_ID),
                UUID.randomUUID().toString().substring(0, 14),
                SpecialtyEnum.FONOAUDIOLOGIA.getName(),
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID() + "@email.com",
                Set.of(ConstantsTests.CLINIC_ID));

        HttpEntity<Object> request = new HttpEntity<>(professional, headers);

        ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ProfessionalDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetAllProfessionalsThenSuccess() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals");

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<List<Clinic>> response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
        });
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldGetProfessionalByIdThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/professionals/" + ConstantsTests.PROFESSIONAL_ID);

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<ProfessionalDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, ProfessionalDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
