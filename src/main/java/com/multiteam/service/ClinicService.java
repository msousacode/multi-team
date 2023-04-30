package com.multiteam.service;

import com.multiteam.controller.dto.request.ClinicRequest;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.repository.ClinicRespository;
import com.multiteam.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ClinicService {

    private final Logger logger = LogManager.getLogger(ClinicService.class);

    private final ClinicRespository clinicRespository;
    private final UserRepository userRepository;

    public ClinicService(ClinicRespository clinicRespository, UserRepository userRepository) {
        this.clinicRespository = clinicRespository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createClinic(ClinicRequest clinicRequest) {

        var user = userRepository.findById(clinicRequest.userId());

        if (user.isEmpty()) {
            logger.debug("check if user exists. userId: {}", clinicRequest.userId());
            logger.error("empty user cannot create clinic userId: {}", clinicRequest.userId());
            return Boolean.FALSE;
        }

        var builder = new Clinic.Builder(
                clinicRequest.clinicName(),
                clinicRequest.cpfCnpj(),
                clinicRequest.email(),
                clinicRequest.cellPhone(),
                user.get())
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();

        clinicRespository.save(builder);

        logger.info("clinic {} created by userId {}", builder.toString(), clinicRequest.userId());

        return Boolean.TRUE;
    }

    public List<Clinic> getAllClinic(UUID ownerId) {
        return clinicRespository.findAllByUserId(ownerId);
    }

    public Optional<Clinic> getClinicById(UUID clinicId) {
        return clinicRespository.findById(clinicId);
    }

    public List<Clinic> getClinics(Set<UUID> clinics) {
        return clinicRespository.findAllById(clinics);
    }

    @Transactional
    public Boolean updateClinic(ClinicRequest clinicRequest) {

        var clinicResult = clinicRespository.findById(clinicRequest.id());

        if (clinicResult.isEmpty()) {
            logger.debug("check if professional exists. professionalId: {}", clinicRequest.id());
            logger.error("professional cannot be null. professionalId: {}", clinicRequest.id());
            return Boolean.FALSE;
        }

        var builder = new Clinic.Builder(
                clinicRequest.clinicName(),
                clinicRequest.cpfCnpj(),
                clinicRequest.email(),
                clinicRequest.cellPhone(),
                clinicResult.get().getUser())
                .id(clinicRequest.id())
                .build();

        clinicRespository.save(builder);

        logger.info("updated clinic: {}", builder.toString());

        return Boolean.TRUE;
    }
}
