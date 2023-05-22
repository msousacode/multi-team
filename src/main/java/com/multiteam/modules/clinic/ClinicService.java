package com.multiteam.modules.clinic;

import com.multiteam.modules.clinic.dto.ClinicDTO;
import com.multiteam.modules.clinic.dto.ClinicUseInCacheResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ClinicService {

    private final Logger logger = LogManager.getLogger(ClinicService.class);

    private final ClinicRepository clinicRepository;

    public ClinicService(final ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Transactional
    public Boolean createClinic(final ClinicDTO clinicDTO) {

        var builder = new Clinic.Builder(
                clinicDTO.clinicName(),
                clinicDTO.cpfCnpj(),
                clinicDTO.email(),
                clinicDTO.cellPhone())
                .telephone(clinicDTO.telephone())
                .observation(clinicDTO.observation())
                .active(true)
                .build();

        clinicRepository.save(builder);

        logger.info("clinic created {}", builder.toString());

        return Boolean.TRUE;
    }

    public List<ClinicDTO> getAllClinic() {
        return clinicRepository.findAll().stream().map(ClinicDTO::new).toList();
    }

    public Optional<Clinic> getClinicById(UUID clinicId) {
        return clinicRepository.findOneById(clinicId);
    }

    public List<Clinic> getClinics(final Set<UUID> clinics) {
        return clinicRepository.findAllById(clinics);
    }

    @Transactional
    public Boolean updateClinic(final ClinicDTO clinicDTO) {

        var clinicResult = clinicRepository.findOneById(clinicDTO.id());

        if (clinicResult.isEmpty()) {
            logger.debug("check if clinic exists. clinicId: {}", clinicDTO.id());
            logger.error("clinic cannot be null. clinicId: {}", clinicDTO.id());
            return Boolean.FALSE;
        }

        var builder = new Clinic.Builder(
                clinicDTO.clinicName(),
                clinicDTO.cpfCnpj(),
                clinicDTO.email(),
                clinicDTO.cellPhone())
                .telephone(clinicDTO.telephone())
                .observation(clinicDTO.observation())
                .id(clinicDTO.id())
                .build();

        clinicRepository.save(builder);

        logger.info("updated clinic: {}", builder.toString());

        return Boolean.TRUE;
    }

    public List<ClinicUseInCacheResponse> getAllClinicsUseInCache() {
        return clinicRepository.findAll().stream().map(ClinicUseInCacheResponse::new).toList();
    }
}
