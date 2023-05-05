package com.multiteam.service;

import com.multiteam.controller.dto.PatientDTO;
import com.multiteam.core.enums.SexEnum;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.repository.PatientRepository;
import com.multiteam.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.multiteam.core.enums.AuthProviderEnum.local;

@Service
public class PatientService {

    private final Logger logger = LogManager.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final TreatmentService treatmentService;
    private final UserService userService;
    private final EmailService emailService;

    public PatientService(
            PatientRepository patientRepository,
            TreatmentService treatmentService,
            UserService userService,
            EmailService emailService
    ) {
        this.patientRepository = patientRepository;
        this.treatmentService = treatmentService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Transactional
    public Boolean createPatient(PatientDTO patientRequest) {

        var owner = userService.getOwnerById(patientRequest.ownerId());

        if (owner.isEmpty()) {
            logger.debug("check if owner exists. ownerId: {}", patientRequest.ownerId());
            logger.error("owner cannot be null. ownerId: {}", patientRequest.ownerId());
            return Boolean.FALSE;
        }

        var user = userService.createUser(patientRequest.name(), patientRequest.email(), local);

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

        if(emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())){
            logger.warn("user was created , but an error occurred when sending the first login email: {}", user.getEmail());
        }
        return Boolean.TRUE;
    }

    public Optional<Patient> getPatient(final UUID patientId, final UUID ownerId) {
        return patientRepository.findByIdAndOwnerId(patientId, ownerId);
    }

    public List<PatientsProfessionalsView> getAllPatientsByProfessionalId(UUID professionalId, SituationEnum situation) {
        //return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
        return List.of();
    }

    public Page<PatientDTO> getAllPatientsByOwnerId(UUID ownerId, Pageable pageable) {
        return patientRepository.findAllByOwnerIdAndActiveIsTrue(ownerId, pageable).map(PatientDTO::fromPatientDTO);
    }

    @Transactional
    public Boolean inactivePatient(UUID patientId, UUID ownerId) {

        var patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            return Boolean.FALSE;
        }

        patientRepository.inactivePatient(patientId, ownerId);

        treatmentService.excludeTreatmentByPatientId(patientId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updatePatient(PatientDTO patientRequest) {

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
