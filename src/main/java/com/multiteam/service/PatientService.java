package com.multiteam.service;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.repository.PatientRepository;
import com.multiteam.persistence.types.SituationType;
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
    public Boolean createPatient(Patient patient, UUID clinicId) {

        var clinic = clinicService.getClinicById(clinicId);

        if (clinic.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Patient.Builder(
                patient.getName(),
                patient.getMiddleName(),
                patient.getSex(),
                patient.getAge(),
                clinic.get())
                .months(patient.getMonths())
                .internalObservation(patient.getInternalObservation())
                .externalObservation(patient.getExternalObservation())
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

    public List<PatientsProfessionalsView> getAllPatientsByProfessionalId(UUID professionalId, SituationType situation) {
        return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
    }

    public List<PatientsProfessionalsView> getAllPatientsByClinicId(UUID clinicId, SituationType situation) {
        return patientRepository.findAllPatientsByClinicId(clinicId, situation);
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
    public Boolean updatePatient(Patient patient) {

        var patientResult = patientRepository.findById(patient.getId());
        var clinicResult = clinicService.getClinicById(patient.getClinic().getId());

        if (patientResult.isPresent() && clinicResult.isPresent()) {

            var builder = new Patient.Builder(
                    patient.getName(),
                    patient.getMiddleName(),
                    patient.getSex(),
                    patient.getAge(),
                    clinicResult.get())
                    .id(patientResult.get().getId())
                    .months(patient.getMonths())
                    .externalObservation(patient.getExternalObservation())
                    .internalObservation(patient.getInternalObservation())
                    .build();

            patientRepository.save(builder);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
