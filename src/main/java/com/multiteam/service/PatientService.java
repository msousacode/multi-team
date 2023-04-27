package com.multiteam.service;

import com.multiteam.controller.dto.request.PatientRequest;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.repository.PatientRepository;
import com.multiteam.enums.SituationEnum;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicService clinicService;
    private final TreatmentService treatmentService;

    public PatientService(
            PatientRepository patientRepository,
            ClinicService clinicService,
            TreatmentService treatmentService
    ) {
        this.patientRepository = patientRepository;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
    }

    @Transactional
    public Boolean createPatient(PatientRequest patientRequest) {

        var clinic = clinicService.getClinicById(patientRequest.clinicId());

        if (clinic.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Patient.Builder(
                patientRequest.name(),
                patientRequest.middleName(),
                patientRequest.sex(),
                patientRequest.age(),
                clinic.get())
                .months(patientRequest.months())
                .internalObservation(patientRequest.internalObservation())
                .externalObservation(patientRequest.externalObservation())
                .build();

        patientRepository.save(builder);

        return Boolean.TRUE;
    }

    public List<Patient> getAllPatientsByClinicId(final UUID clinicId) {
        return patientRepository.findAllByClinic_Id(clinicId);
    }

    public Optional<Patient> getPatientById(final UUID patientId, final UUID clinicId) {
        return patientRepository.findByIdAndClinic_Id(patientId, clinicId);
    }

    public List<PatientsProfessionalsView> getAllPatientsByProfessionalId(UUID professionalId, SituationEnum situation) {
        //return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
        return List.of();
    }

    public List<PatientsProfessionalsView> getAllPatientsByClinicId(UUID clinicId, SituationEnum situation) {
        //return patientRepository.findAllPatientsByClinicId(clinicId, situation);
        return List.of();
    }

    @Transactional
    public Boolean inactivePatient(UUID patientId, UUID clinicId) {

        var patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            return Boolean.FALSE;
        }

        patientRepository.inactivePatient(patientId, clinicId);

        treatmentService.excludeTreatmentByPatientId(patientId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updatePatient(PatientRequest patientRequest) {

        var patientResult = patientRepository.findById(patientRequest.id());
        var clinicResult = clinicService.getClinicById(patientRequest.clinicId());

        if (patientResult.isPresent() && clinicResult.isPresent()) {

            var builder = new Patient.Builder(
                    patientRequest.name(),
                    patientRequest.middleName(),
                    patientRequest.sex(),
                    patientRequest.age(),
                    clinicResult.get())
                    .id(patientResult.get().getId())
                    .months(patientRequest.months())
                    .externalObservation(patientRequest.externalObservation())
                    .internalObservation(patientRequest.internalObservation())
                    .build();

            patientRepository.save(builder);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
