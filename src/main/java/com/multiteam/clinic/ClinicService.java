package com.multiteam.clinic;

import com.multiteam.clinic.dto.ClinicDTO;
import com.multiteam.user.UserRepository;
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

    private final ClinicRepository clinicRespository;
    private final UserRepository userRepository;

    public ClinicService(ClinicRepository clinicRespository, UserRepository userRepository) {
        this.clinicRespository = clinicRespository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createClinic(ClinicDTO clinicRequest) {

        var builder = new Clinic.Builder(
                clinicRequest.clinicName(),
                clinicRequest.cpfCnpj(),
                clinicRequest.email(),
                clinicRequest.cellPhone())
                .active(true)
                .build();

        clinicRespository.save(builder);

        logger.info("clinic created {}", builder.toString());

        return Boolean.TRUE;
    }

    public List<ClinicDTO> getAllClinic() {
        return clinicRespository.findAll()
                .stream().map(i -> new ClinicDTO(
                        i.getId(), i.getClinicName(), i.getCpfCnpj(),
                        i.getEmail(), i.getCellPhone(), i.getTelephone(),
                        i.getObservation())).toList();
    }

    public Optional<Clinic> getClinicById(UUID clinicId) {
        return clinicRespository.findOneById(clinicId);
    }

    public List<Clinic> getClinics(Set<UUID> clinics) {
        return clinicRespository.findAllById(clinics);
    }

    @Transactional
    public Boolean updateClinic(ClinicDTO clinicRequest) {

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
                clinicRequest.cellPhone())
                .id(clinicRequest.id())
                .build();

        clinicRespository.save(builder);

        logger.info("updated clinic: {}", builder.toString());

        return Boolean.TRUE;
    }
}
