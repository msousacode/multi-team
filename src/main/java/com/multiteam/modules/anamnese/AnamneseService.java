package com.multiteam.modules.anamnese;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.modules.anamnese.dto.AnamneseRequest;
import com.multiteam.modules.anamnese.dto.AnamneseReportResponse;
import com.multiteam.modules.anamnese.dto.AnamneseResponse;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnamneseService {

    private final Logger logger = LogManager.getLogger(PatientService.class);

    private final AnamneseRepository anamneseRepository;
    private final PatientService patientService;
    private final UserService userService;
    private final TenantContext tenantContext;

    public AnamneseService(
            final AnamneseRepository anamneseRepository,
            final PatientService patientService,
            UserService userService,
            final TenantContext tenantContext) {
        this.anamneseRepository = anamneseRepository;
        this.patientService = patientService;
        this.userService = userService;
        this.tenantContext = tenantContext;
    }

    @Transactional
    public Boolean createAnamnese(final AnamneseRequest anamneseDTO) {

        var patient = patientService.findOneById(anamneseDTO.patientId());

        if (patient.isEmpty()) {
            logger.error("patient not found. Verify if patient exists, patientId: {}", anamneseDTO.patientId());
            return Boolean.FALSE;
        }

        var builder = new Anamnese.Builder(
                anamneseDTO.id(),
                AnamneseEnum.OPEN,
                patient.get())
                .annotation(anamneseDTO.annotation())
                .conclusion(anamneseDTO.conclusion())
                .active(true)
                .build();

        anamneseRepository.save(builder);

        logger.info("successfully created anamnese {} ", builder.toString());

        return Boolean.TRUE;
    }

    public List<AnamneseResponse> getAllAnamneses(final UUID patientId) {
        return anamneseRepository.findAllByPatientId(patientId).stream().map(AnamneseResponse::new).toList();
    }

    public Optional<AnamneseReportResponse> getAnamneseReport(final UUID anamneseId) {

        var anamnese = anamneseRepository.findOneById(anamneseId);

        if (anamnese.isEmpty()) {
            logger.error("anamnese not found id: {}", anamneseId);
            return Optional.empty();
        }

        var user = userService.getUser(UUID.fromString(anamnese.get().getCreatedBy()));

        if (user.isPresent()) {
            var anamneseReport = AnamneseReportResponse.fromAnamneseReportResponse(anamnese.get(), anamnese.get().getPatient(), user.get());
            return Optional.of(anamneseReport);
        }
        logger.error("error occurred while searching. verify if exists user or anamnese");
        return Optional.empty();
    }

    @Transactional
    public Boolean inactiveAnamnese(final UUID anamneseId) {
        anamneseRepository.inactiveAnamnese(anamneseId, tenantContext.getTenantId());
        return Boolean.TRUE;
    }
}
