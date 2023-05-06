package com.multiteam.controller;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class ClinicControllerTest extends TokenUtil {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetPatientById_thenSuccess() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/v1/clinics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .header("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /*
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void shouldCreateNewClinicThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

        var clinic = new Clinic.Builder(
                UUID.randomUUID() + " @Test",
                UUID.randomUUID().toString().substring(0, 14),
                UUID.randomUUID().toString().substring(0, 15),
                UUID.randomUUID().toString().substring(0, 15))
                .build();

        HttpEntity<Object> request = new HttpEntity<>(clinic, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void shouldGetAllClinicsThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/clinics");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<List<Clinic>> response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
     */
}
