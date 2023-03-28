package com.multiteam.service;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.types.SexType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ClinicService clinicService;

    @Test
    @DisplayName("deve criar um novo paciente e retornar sucesso")
    void shouldCreatePatient_thenSuccess() {

        var clinicDefault = UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2");

        var clinic = clinicService.findById(clinicDefault);

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

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getName(), "Márcio");
        Assertions.assertEquals(result.getMiddleName(), "Farias");
        Assertions.assertNotNull(result.getClinic().getId());
    }

    @Test
    @DisplayName("deve retornar a lista de pacientes filtrando pelo uuid da clinica com sucesso")
    void shouldReturnListPacientFilteringByIdClinic_thenSuccess() {
        var result = patientService.getAllPatients(UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2"));
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar o paciente filtrando pelo uuid do paciente e da clinica com sucesso")
    void shouldReturnListPacientFilteringByIdPacientAndIdClinic_thenSuccess() {
        var patientId = UUID.fromString("bc91b5cc-3bb8-48f4-abdf-8f16e33d6787");
        var clinicId = UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2");

        var result = patientService.getPatientById(patientId, clinicId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get().getId(), patientId);
        Assertions.assertEquals(result.get().getClinic().getId(), clinicId);
    }

    @Test
    @DisplayName("dado um patientId que não existe deve retornar vazio")
    void givenPatientIdThatNotExists_thenReturnEmpty() {
        var result = patientService.getPatientById(UUID.fromString("7bdb248d-5f38-4060-8bb7-4a4f98a0ab51"), UUID.fromString("9667823d-d5db-4387-bb5a-06e0278795f2"));
        Assertions.assertTrue(result.isEmpty());
    }
}
