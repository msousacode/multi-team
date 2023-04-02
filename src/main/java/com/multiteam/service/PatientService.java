package com.multiteam.service;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.repository.PatientRepository;
import com.multiteam.persistence.types.SituationType;
import com.multiteam.vo.DataResponse;
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

    public List<Patient> getAllPatients(final UUID clinicId) {
        return patientRepository.findAllByClinic_Id(clinicId);
    }

    public Optional<Patient> getPatientById(final UUID patientId, final UUID clinicId) {
        return patientRepository.findByIdAndClinic_Id(patientId, clinicId);
    }

    public List<PatientsProfessionalsView> findAllPatientsByProfessionalId(UUID professionalId, SituationType situation) {
        return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
    }

    public List<PatientsProfessionalsView> findAllPatientsByClinicId(UUID professionalId, SituationType situation) {
        return patientRepository.findAllPatientsByClinicId(professionalId, situation);
    }

    @Transactional
    public DataResponse inactivePatient(UUID patientId, UUID clinicId) {

        var patient = patientRepository.findById(patientId);

        if (patient.isEmpty())
            return new DataResponse(null, "patient not found", false);

        patientRepository.inactivePatient(patientId, clinicId);

        treatmentService.excludeTreatmentByPatientId(patientId);

        return new DataResponse(null, "patient excluded successfully", true);
    }

    @Transactional
    public DataResponse updatePatient(Patient patient) {

        var patientResult = patientRepository.findById(patient.getId());
        var clinicResult = clinicService.findById(patient.getClinic().getId());

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
            return new DataResponse(null, "updated with success", true);
        }

        return new DataResponse(null, "resource not found", false);
    }
}
