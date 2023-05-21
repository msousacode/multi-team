package com.multiteam.controller;

import com.multiteam.core.enums.RoleEnum;
import com.multiteam.modules.schedule.ScheduleRequest;
import com.multiteam.util.ConstantsTest;
import com.multiteam.util.RestTemplateBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleControllerTest extends RestTemplateBase {

    @Test
    void shouldCreateNewScheduleThenSuccess() throws Exception {

        URI uri = new URI("http://localhost:" + port + "/team/v1/schedules");

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Brazil/East"), new Locale("pt", "BR"));

       var schedule = new ScheduleRequest(
               UUID.fromString(ConstantsTest.PATIENT_ID),
               UUID.fromString(ConstantsTest.CLINIC_ID),
               UUID.fromString(ConstantsTest.PROFESSIONAL_ID),
               "Test@@ Test@@ Test@@ Test@@ Test@@ Test@@",
               calendar,
               null,
               "https:Teste@@@@",
               "Description Description Description Description Description Description Description ",
               null,
               null
       );

        headers.set("Authorization", "Bearer " + getToken(RoleEnum.ROLE_OWNER));
        HttpEntity<Object> request = new HttpEntity<>(schedule, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, null);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }
}
