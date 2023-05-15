package com.multiteam.anamnese;

import com.multiteam.anamnese.dto.AnamneseDTO;
import com.multiteam.anamnese.dto.AnamneseResponseDTO;
import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.patient.PatientService;
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
    private final TenantContext tenantContext;

    public AnamneseService(
            final AnamneseRepository anamneseRepository,
            final PatientService patientService,
            final TenantContext tenantContext) {
        this.anamneseRepository = anamneseRepository;
        this.patientService = patientService;
        this.tenantContext = tenantContext;
    }

    @Transactional
    public Boolean createAnamnese(final AnamneseDTO anamneseDTO) {

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

    public List<AnamneseResponseDTO> getAllAnamneses(final UUID patientId) {
        return anamneseRepository.findAllByPatientId(patientId).stream().map(AnamneseResponseDTO::new).toList();
    }

    public Optional<AnamneseDTO> getAnamnese(final UUID anamneseId) {
        return anamneseRepository.findOneById(anamneseId).map(AnamneseDTO::new);
    }

    @Transactional
    public Boolean inactiveAnamnese(final UUID anamneseId) {
        anamneseRepository.inactiveAnamnese(anamneseId, tenantContext.getTenantId());
        return Boolean.TRUE;
    }
}