package com.multiteam.modules.patient;

import static com.multiteam.core.enums.AuthProviderEnum.local;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.SexEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.service.EmailService;
import com.multiteam.modules.patient.controller.dto.PatientDTO;
import com.multiteam.modules.patient.controller.dto.PatientFilter;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.patient.repository.PatientRepository;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.user.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    var user = userService.createUser(patientDTO.name(), patientDTO.email(), local,
        UserEnum.PACIENTE);

    if (user == null) {
      logger.error("An error occurred while creating the user, email: {}", patientDTO.email());
      return Boolean.FALSE;
    }

    var builder = new Patient.Builder(
        patientDTO.name(),
        SexEnum.get(patientDTO.sex()),
        patientDTO.cpf(),
        patientDTO.age(),
        patientDTO.dateBirth(),
        user)
        .active(true)
        .cellPhone(patientDTO.cellPhone())
        .build();

    patientRepository.save(builder);

    logger.info("successfully created patient {} ", builder.toString());

        /* TODO código foi comentado, pois será utilizado em outro momento, quando a plataforma aceitar o acesso do paciente para eventuais consultas.
        if (emailService.sendEmailNewUser(user.getEmail(), user.getProvisionalPassword())) {
            logger.warn("user was created , but an error occurred when sending the first login email: {}", user.getEmail());
        }*/
    return Boolean.TRUE;
  }

  public Optional<Patient> getPatientById(final UUID patientId) {
    return Optional.ofNullable(patientRepository.findOneById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(ApplicationError.RESOURCE_NOT_FOUND.getMessage(), patientId))));
  }

  public Page<PatientDTO> findAllTreatmentAndSituationProgressByProfessionalId(final PatientFilter patientFilter, Pageable pageable) {
    return patientRepository.findAllByNameContainingIgnoreCaseAndActiveIsTrue(patientFilter.patientName(),
        pageable).map(PatientDTO::fromPatientDTO);
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

    var patient = patientRepository.findById(patientDTO.id());

    if (patient.isPresent()) {

      patient.get().setName(patientDTO.name());
      patient.get().setAge(patientDTO.age());
      patient.get().setDateBirth(patientDTO.dateBirth());
      patient.get().setCellPhone(patientDTO.cellPhone());
      patient.get().setSex(SexEnum.get(patientDTO.sex()));

      patientRepository.save(patient.get());

      logger.info("successfully updated patient {} ", new Patient().toString());

      return Boolean.TRUE;

    } else {
      logger.error("error occurred while updating the patient: {}", patientDTO.id());
      return Boolean.FALSE;
    }
  }

  public List<Patient> findAllTreatmentAndSituationProgressByProfessionalId(UUID professionalId) {
    return patientRepository.findAllTreatmentAndSituationProgressByProfessionalId(professionalId);
  }
}
