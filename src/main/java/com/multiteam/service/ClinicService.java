package com.multiteam.service;

import com.multiteam.controller.dto.request.ClinicRequest;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.repository.ClinicRespository;
import com.multiteam.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicService {

    private final ClinicRespository clinicRespository;
    private final UserRepository userRepository;

    public ClinicService(ClinicRespository clinicRespository, UserRepository userRepository) {
        this.clinicRespository = clinicRespository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createClinic(ClinicRequest clinicRequest) {

        var user = userRepository.findById(clinicRequest.userId());

        if (user.isEmpty()){
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

        return Boolean.TRUE;
    }

    public List<Clinic> getAllClinic(UUID ownerId) {
        return clinicRespository.findAllByUserId(ownerId);
    }

    public Optional<Clinic> getClinicById(UUID clinicId) {
        return clinicRespository.findById(clinicId);
    }
}
