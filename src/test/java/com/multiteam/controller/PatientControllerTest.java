package com.multiteam.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.modules.patient.controller.dto.PatientDTO;
import com.multiteam.modules.patient.controller.dto.PatientFilter;
import com.multiteam.modules.professional.dto.ProfessionalDTO;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestResponsePage;
import com.multiteam.util.RestTemplateBase;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PatientControllerTest extends RestTemplateBase {

  /*
  @Test
  void shouldCreateNewPatientThenSuccess() throws Exception {

    URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

    headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

    var patient = new PatientDTO(
        null,
        UUID.randomUUID().toString(),
        UUID.randomUUID() + "@test.com",
        UUID.randomUUID().toString().substring(0, 14),
        SexEnum.NAO_DECLARADO.getDescription(),
        20,
        LocalDate.now(),
        "");

    HttpEntity<Object> request = new HttpEntity<>(patient, headers);

    ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);
  }

  @Test
  void shouldUpdatedPatientThenSuccess() throws Exception {

    URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

    headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

    var patient = new PatientDTO(
        UUID.fromString(ConstantsTest.PATIENT_ID),
        UUID.randomUUID().toString(),
        UUID.randomUUID() + "@test.com",
        UUID.randomUUID().toString().substring(0, 14),
        SexEnum.NAO_DECLARADO.getDescription(),
        20,
        LocalDate.now(),
        "");

    HttpEntity<Object> request = new HttpEntity<>(patient, headers);

    ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.PUT, request,
        ClinicDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }
   */

  @Test
  void shouldGetAllPatientsThenSuccess() throws Exception {

    URI uri = new URI("http://localhost:" + port + "/team/v1/patients");

    headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
    HttpEntity<Object> request = new HttpEntity<>(headers);

    ParameterizedTypeReference<RestResponsePage<PatientDTO>> responseType = new ParameterizedTypeReference<>() {
    };
    ResponseEntity<RestResponsePage<PatientDTO>> response = restTemplate.exchange(uri,
        HttpMethod.GET, request, responseType);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  void shouldGetPatientByIdThenSuccess() throws Exception {

    URI uri = new URI(
        "http://localhost:" + port + "/team/v1/professionals/" + ConstantsTest.PROFESSIONAL_ID);

    headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
    HttpEntity<Object> request = new HttpEntity<>(headers);

    ResponseEntity<ProfessionalDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request,
        ProfessionalDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  void findAllTreatmentAndSituationProgressByProfessionalId_ThenSuccess() throws Exception {

    URI uri = new URI("http://localhost:" + port + "/team/v1/patients/mobile/filter");

    PatientFilter filter = new PatientFilter("", UUID.fromString(ConstantsTest.PROFESSIONAL_ID));

    headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));

    HttpEntity<Object> request = new HttpEntity<>(filter, headers);

    ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(uri, HttpMethod.POST, request,
        new ParameterizedTypeReference<>() {
        });

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }
}
