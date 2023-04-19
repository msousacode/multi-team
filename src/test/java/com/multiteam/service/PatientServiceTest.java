package com.multiteam.service;

import com.multiteam.constants.Constants;
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

        var clinicDefault = Constants.CLINIC_ID;

        var clinic = clinicService.getClinicById(clinicDefault);

        var builder = new Patient.Builder(
                "Márcio",
                "Farias",
                SexType.MASCULINO,
                13,
                clinic.get())
                .months(null)
                .internalObservation("Lorem ipsum nulla sollicitudin massa lectus enim justo ligula, nulla inceptos nisi curabitur libero bibendum class mollis, cras metus maecenas dictum sollicitudin class senectus. arcu molestie conubia dui conubia quis condimentum ipsum tellus ornare habitant inceptos aliquet elit, tempor porttitor etiam magna neque tincidunt nulla torquent euismod rhoncus facilisis. nisi dui maecenas enim risus in lectus porta, lacinia")
                .externalObservation("Lorem ipsum nulla sollicitudin massa lectus enim justo ligula, nulla inceptos nisi curabitur libero bibendum class mollis, cras metus maecenas dictum sollicitudin class senectus. arcu molestie conubia dui conubia quis condimentum ipsum tellus ornare habitant inceptos aliquet elit, tempor porttitor etiam magna neque tincidunt nulla torquent euismod rhoncus facilisis. nisi dui maecenas enim risus in lectus porta, lacinia")
                .build();

        var result = patientService.createPatient(builder, clinicDefault);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deve retornar a lista de pacientes filtrando pelo uuid da clinica com sucesso")
    void shouldReturnListPatientFilteringByIdClinic_thenSuccess() {
        var result = patientService.getAllPatientsByClinicId(Constants.CLINIC_ID);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar o paciente filtrando pelo uuid do paciente e da clinica com sucesso")
    void shouldReturnListPatientFilteringByIdPacientAndIdClinic_thenSuccess() {
        var patientId = Constants.PATIENT_ID;
        var clinicId = Constants.CLINIC_ID;

        var result = patientService.getPatientById(patientId, clinicId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get().getId(), patientId);
        Assertions.assertEquals(result.get().getClinic().getId(), clinicId);
    }

    @Test
    @DisplayName("deve retornar todos os pacientes filtrados por professionalId então sucesso")
    void shouldReturnAllPatientsFilteringByProfessionalId_thenSuccess() {
        var result = patientService.getAllPatientsByProfessionalId(Constants.PROFESSIONAL_ID, SituationType.ANDAMENTO);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar todos os pacientes filtrados por clinicId então sucesso")
    void shouldReturnAllPatientsFilteringByClinicId_thenSuccess() {
        var result = patientService.getAllPatientsByClinicId(Constants.CLINIC_ID, SituationType.ANDAMENTO);
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

        var clinicBuilder = new Clinic.Builder(
                "Geraldo Bryan Gomes",
                "35997818268",
                "email@email.com",
                "(71) 3871-3197")
                .id(Constants.CLINIC_ID)
                .build();

        var patientBuilder = new Patient.Builder(
                "Isabela",
                "Freitas",
                SexType.MASCULINO,
                15,
                clinicBuilder)
                .id(Constants.PATIENT_ID)
                .active(true)
                .months(2)
                .externalObservation("Observation test unit")
                .internalObservation("Observation test unit")
                .build();

        var response = patientService.updatePatient(patientBuilder);

        Assertions.assertTrue(response);
    }
}
