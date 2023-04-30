package com.multiteam.service;

import com.multiteam.controller.dto.request.PatientRequest;
import com.multiteam.enums.AuthProviderEnum;
import com.multiteam.enums.SexEnum;
import com.multiteam.enums.SituationEnum;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.entity.User;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.repository.PatientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.multiteam.enums.AuthProviderEnum.local;

@Service
public class PatientService {

    private final Logger logger = LogManager.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final ClinicService clinicService;
    private final TreatmentService treatmentService;
    private final UserService userService;

    public PatientService(
            PatientRepository patientRepository,
            ClinicService clinicService,
            TreatmentService treatmentService,
            UserService userService
    ) {
        this.patientRepository = patientRepository;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
        this.userService = userService;
    }

    @Transactional
    public Boolean createPatient(PatientRequest patientRequest) {

        var owner = userService.getOwnerById(patientRequest.ownerId());

        if (owner.isEmpty()) {
            logger.debug("check if owner exists. ownerId: {}", patientRequest.ownerId());
            logger.error("owner cannot be null. ownerId: {}", patientRequest.ownerId());
            return Boolean.FALSE;
        }

        var user = userService.createUser(patientRequest.name(), patientRequest.email(), patientRequest.ownerId(), local);

        if (user == null) {
            logger.error("An error occurred while creating the user, email: {}, ownerId: {}", patientRequest.email(), patientRequest.ownerId());
            return Boolean.FALSE;
        }

        var builder = new Patient.Builder(
                owner.get().getId(),
                patientRequest.name(),
                SexEnum.get(patientRequest.sex()),
                patientRequest.age(),
                patientRequest.dateBirth(),
                user)
                .active(true)
                .cellPhone(patientRequest.cellPhone())
                .build();

        patientRepository.save(builder);

        logger.info("successfully created patient {} ", builder.toString());

        return Boolean.TRUE;
    }

    public List<Patient> getAllPatientsByOwnerId(final UUID ownerId) {
        return patientRepository.findAllByOwnerId(ownerId);
    }

    public Optional<Patient> getPatientById(final UUID patientId, final UUID clinicId) {
        return patientRepository.findById(patientId);
    }

    public List<PatientsProfessionalsView> getAllPatientsByProfessionalId(UUID professionalId, SituationEnum situation) {
        //return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
        return List.of();
    }

    public List<PatientsProfessionalsView> getAllPatientsByOwnerId(UUID clinicId, SituationEnum situation) {
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

        if (patientResult.isPresent()) {

            var builder = new Patient.Builder(
                    patientResult.get().getOwnerId(),
                    patientRequest.name(),
                    SexEnum.get(patientRequest.sex()),
                    patientRequest.age(),
                    patientRequest.dateBirth(),
                    patientResult.get().getUser())
                    .id(patientResult.get().getId())
                    .cellPhone(patientRequest.cellPhone())
                    .active(true)
                    .build();

            patientRepository.save(builder);

            logger.info("successfully updated patient {} ", new Patient().toString());

            return Boolean.TRUE;

        } else {
            logger.error("error occurred while updating the patient: {}", patientRequest.ownerId());
            return Boolean.FALSE;
        }
    }
}
