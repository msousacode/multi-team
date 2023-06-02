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
                .active(Boolean.TRUE)
                .site(clinicDTO.site())
                .inscEstadual(clinicDTO.inscEstadual())
                .city(clinicDTO.city())
                .address(clinicDTO.address())
                .district(clinicDTO.district())
                .number(clinicDTO.number())
                .state(clinicDTO.state())
                .zipCode(clinicDTO.zipCode())
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

        var clinic = clinicRepository.findOneById(clinicDTO.id());

        if (clinic.isEmpty()) {
            logger.debug("check if clinic exists. clinicId: {}", clinicDTO.id());
            logger.error("clinic cannot be null. clinicId: {}", clinicDTO.id());
            return Boolean.FALSE;
        }

        clinic.get().setClinicName(clinicDTO.clinicName());
        clinic.get().setCpfCnpj(clinicDTO.cpfCnpj());
        clinic.get().setEmail(clinicDTO.email());
        clinic.get().setCellPhone(clinicDTO.cellPhone());
        clinic.get().setTelephone(clinicDTO.telephone());
        clinic.get().setObservation(clinicDTO.observation());
        clinic.get().setZipCode(clinicDTO.zipCode());
        clinic.get().setAddress(clinicDTO.address());
        clinic.get().setInscEstadual(clinicDTO.inscEstadual());
        clinic.get().setNumber(clinicDTO.number());
        clinic.get().setCity(clinicDTO.city());
        clinic.get().setState(clinicDTO.state());

        clinicRepository.save(clinic.get());

        logger.info("updated clinic: {}", clinic.get().toString());

        return Boolean.TRUE;
    }

    public List<ClinicUseInCacheResponse> getAllClinicsUseInCache() {
        return clinicRepository.findAll().stream().map(ClinicUseInCacheResponse::new).toList();
    }
}
