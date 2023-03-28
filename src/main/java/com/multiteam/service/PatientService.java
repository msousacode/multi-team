package com.multiteam.service;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.repository.PatientRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicService clinicService;

    public PatientService(
            PatientRepository patientRepository,
            ClinicService clinicService) {
        this.patientRepository = patientRepository;
        this.clinicService = clinicService;
    }

    @Transactional
    public Patient createPatient(Patient patient, UUID clinicId) {

        var clinic = clinicService.findById(clinicId);

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

        return patientRepository.save(builder);
    }

    public List<Patient> findAll(final UUID clinicId) {
        return patientRepository.findAllByClinic_Id(clinicId);
    }

    public Optional<Patient> findById(final UUID patientId, final UUID clinicId) {
        return patientRepository.findByIdAndClinic_Id(patientId, clinicId);
    }
}
