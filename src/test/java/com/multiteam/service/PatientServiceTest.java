package com.multiteam.service;

import com.multiteam.constants.ConstantsToTests;
import com.multiteam.controller.dto.PatientRequest;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.enums.SexType;
import com.multiteam.persistence.enums.SituationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar um novo paciente e retornar sucesso")
    void shouldCreatePatient_thenSuccess() {

        var patientRequest = new PatientRequest(
                null,
                "Márcio",
                "Farias",
                SexType.MASCULINO,
                13,
                10,
                "Lorem ipsum nulla sollicitudin massa lectus enim justo ligula, nulla inceptos nisi curabitur libero bibendum class mollis, cras metus maecenas dictum sollicitudin class senectus. arcu molestie conubia dui conubia quis condimentum ipsum tellus ornare habitant inceptos aliquet elit, tempor porttitor etiam magna neque tincidunt nulla torquent euismod rhoncus facilisis. nisi dui maecenas enim risus in lectus porta, lacinia",
                "Lorem ipsum nulla sollicitudin massa lectus enim justo ligula, nulla inceptos nisi curabitur libero bibendum class mollis, cras metus maecenas dictum sollicitudin class senectus. arcu molestie conubia dui conubia quis condimentum ipsum tellus ornare habitant inceptos aliquet elit, tempor porttitor etiam magna neque tincidunt nulla torquent euismod rhoncus facilisis. nisi dui maecenas enim risus in lectus porta, lacinia",
                true,
                ConstantsToTests.CLINIC_ID);

        var result = patientService.createPatient(patientRequest);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deve retornar a lista de pacientes filtrando pelo uuid da clinica com sucesso")
    void shouldReturnListPatientFilteringByIdClinic_thenSuccess() {
        var result = patientService.getAllPatientsByClinicId(ConstantsToTests.CLINIC_ID);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar o paciente filtrando pelo uuid do paciente e da clinica com sucesso")
    void shouldReturnListPatientFilteringByIdPacientAndIdClinic_thenSuccess() {
        var patientId = ConstantsToTests.PATIENT_ID;
        var clinicId = ConstantsToTests.CLINIC_ID;

        var result = patientService.getPatientById(patientId, clinicId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get().getId(), patientId);
        Assertions.assertEquals(result.get().getClinic().getId(), clinicId);
    }

    @Test
    @DisplayName("deve retornar todos os pacientes filtrados por professionalId então sucesso")
    void shouldReturnAllPatientsFilteringByProfessionalId_thenSuccess() {
        var result = patientService.getAllPatientsByProfessionalId(ConstantsToTests.PROFESSIONAL_ID, SituationType.ANDAMENTO);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar todos os pacientes filtrados por clinicId então sucesso")
    void shouldReturnAllPatientsFilteringByClinicId_thenSuccess() {
        var result = patientService.getAllPatientsByClinicId(ConstantsToTests.CLINIC_ID, SituationType.ANDAMENTO);
        Assertions.assertFalse(result.isEmpty());
    }
/*
    @Test
    @DisplayName("Deve inativar o paciente marcando o mesmo com o flag false e de modo cascata inativar o tratamento associados ao mesmo.")
    void shouldInactivePatientByPatientId_thenSuccess() {
        var result = patientService.inactivePatient(TestsConstants.PATIENT_ID, TestsConstants.CLINIC_ID);
        Assertions.assertTrue(result.success());
    }
*/
    @Test
    @DisplayName("Deve atualizar o paciente então sucesso")
    void shouldUpdatePatient_thenSuccess() {

        var patientRequest = new PatientRequest(
                ConstantsToTests.PATIENT_ID,
                "Isabela",
                "Freitas",
                SexType.MASCULINO,
                15,
                2,
                "Observation test unit",
                "Observation test unit",
                true,
                ConstantsToTests.CLINIC_ID);

        var response = patientService.updatePatient(patientRequest);

        Assertions.assertTrue(response);
    }
}
