package com.multiteam.service;

import com.multiteam.persistence.entity.Clinic;
import com.multiteam.persistence.repository.ClinicRespository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicService {

    private final ClinicRespository clinicRespository;

    public ClinicService(ClinicRespository clinicRespository) {
        this.clinicRespository = clinicRespository;
    }

    @Transactional
    public Clinic createClinic(Clinic clinic) {

        var builder = new Clinic.Builder(
                clinic.getClinicName(),
                clinic.getCpfCnpj(),
                clinic.getEmail(),
                clinic.getCellPhone())
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();

        return clinicRespository.save(builder);
    }

    public List<Clinic> getAllClinic() {
        return clinicRespository.findAll();
    }

    public Optional<Clinic> getClinicById(UUID clinicId) {
        return clinicRespository.findById(clinicId);
    }
}
