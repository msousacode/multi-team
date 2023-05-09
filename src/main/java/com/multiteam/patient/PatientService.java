package com.multiteam.patient;

import com.multiteam.core.enums.SexEnum;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.service.EmailService;
import com.multiteam.treatment.TreatmentService;
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

        var user = userService.createUser(patientRequest.name(), patientRequest.email(), local);

        if (user == null) {
            logger.error("An error occurred while creating the user, email: {}", patientRequest.email());
            return Boolean.FALSE;
        }

        var builder = new Patient.Builder(
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

        if (emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())) {
            logger.warn("user was created , but an error occurred when sending the first login email: {}", user.getEmail());
        }
        return Boolean.TRUE;
    }

    public Optional<Patient> findOneById(UUID patientId) {
        return patientRepository.findOneById(patientId);
    }

    public List<PatientsProfessionalsView> getAllPatientsByProfessionalId(UUID professionalId, SituationEnum situation) {
        //return patientRepository.findAllPatientsByProfessionalId(professionalId, situation);
        return List.of();
    }

    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable).map(PatientDTO::fromPatientDTO);
    }

    @Transactional
    public Boolean inactivePatient(UUID patientId) {

        var patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            return Boolean.FALSE;
        }

        patientRepository.inactivePatient(patientId);

        treatmentService.excludeTreatmentByPatientId(patientId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updatePatient(PatientDTO patientRequest) {

        var patientResult = patientRepository.findById(patientRequest.id());

        if (patientResult.isPresent()) {

            var builder = new Patient.Builder(
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
            logger.error("error occurred while updating the patient: {}", patientRequest.id());
            return Boolean.FALSE;
        }
    }
}
