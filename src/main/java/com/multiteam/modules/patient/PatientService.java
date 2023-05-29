package com.multiteam.modules.patient;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.SexEnum;
import com.multiteam.core.service.EmailService;
import com.multiteam.modules.patient.dto.PatientDTO;
import com.multiteam.modules.patient.dto.PatientFilter;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.user.UserService;
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
    private final TenantContext tenantContext;

    public PatientService(
            final PatientRepository patientRepository,
            final TreatmentService treatmentService,
            final UserService userService,
            final EmailService emailService,
            final TenantContext tenantContext
    ) {
        this.patientRepository = patientRepository;
        this.treatmentService = treatmentService;
        this.userService = userService;
        this.emailService = emailService;
        this.tenantContext = tenantContext;
    }

    @Transactional
    public Boolean createPatient(final PatientDTO patientDTO) {

        var user = userService.createUser(patientDTO.name(), patientDTO.email(), local);

        if (user == null) {
            logger.error("An error occurred while creating the user, email: {}", patientDTO.email());
            return Boolean.FALSE;
        }

        var builder = new Patient.Builder(
                patientDTO.name(),
                SexEnum.get(patientDTO.sex()),
                patientDTO.age(),
                patientDTO.dateBirth(),
                user)
                .active(true)
                .cellPhone(patientDTO.cellPhone())
                .build();

        patientRepository.save(builder);

        logger.info("successfully created patient {} ", builder.toString());

        if (emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())) {
            logger.warn("user was created , but an error occurred when sending the first login email: {}", user.getEmail());
        }
        return Boolean.TRUE;
    }

    public Optional<Patient> findOneById(final UUID patientId) {
        return patientRepository.findOneById(patientId);
    }

    public Page<PatientDTO> getAllPatients(final PatientFilter patientFilter, Pageable pageable) {
        return patientRepository.findAllByNameContainingIgnoreCase(patientFilter.patientName(), pageable).map(PatientDTO::fromPatientDTO);
    }

    @Transactional
    public Boolean inactivePatient(final UUID patientId) {

        var patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            logger.error("patient not found. Verify if patient exists, patientId: {}", patientId);
            return Boolean.FALSE;
        }

        patientRepository.inactivePatient(patientId, tenantContext.getTenantId());

        treatmentService.excludeTreatmentByPatientId(patientId);

        logger.info("successfully inactive patientId: {} ", patientId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updatePatient(final PatientDTO patientDTO) {

        var patientResult = patientRepository.findById(patientDTO.id());

        if (patientResult.isPresent()) {

            var builder = new Patient.Builder(
                    patientDTO.name(),
                    SexEnum.get(patientDTO.sex()),
                    patientDTO.age(),
                    patientDTO.dateBirth(),
                    patientResult.get().getUser())
                    .id(patientResult.get().getId())
                    .cellPhone(patientDTO.cellPhone())
                    .active(true)
                    .build();

            patientRepository.save(builder);

            logger.info("successfully updated patient {} ", new Patient().toString());

            return Boolean.TRUE;

        } else {
            logger.error("error occurred while updating the patient: {}", patientDTO.id());
            return Boolean.FALSE;
        }
    }
}
