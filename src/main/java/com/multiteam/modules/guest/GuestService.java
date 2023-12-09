package com.multiteam.modules.guest;


import com.multiteam.core.enums.RelationshipEnum;
import com.multiteam.core.exception.TreatmentException;
import com.multiteam.modules.guest.dto.GuestPostDTO;
import com.multiteam.modules.guest.mapper.GuestMapper;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class GuestService {

    private final GuestRespository guestRespository;
    private final TreatmentService treatmentService;
    private final UserRepository userRepository;
    private final PatientService patientService;

    public GuestService(
            GuestRespository guestRespository,
            TreatmentService treatmentService,
            UserRepository userRepository,
            PatientService patientService) {
        this.guestRespository = guestRespository;
        this.treatmentService = treatmentService;
        this.userRepository = userRepository;
        this.patientService = patientService;
    }

    @Transactional
    public Boolean createGuest(GuestPostDTO guestPostDTO, UUID patientId) {

        var patient = patientService.getPatientById(patientId);
        var guest = GuestMapper.MAPPER.toEntity(guestPostDTO);

        guest.setPatient(patient.get());
        guest.setActive(true);

        guestRespository.save(guest);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean addGuestInTreatment(UUID patientId, UUID guestId) {

        var treatments = treatmentService.getAllTreatments(null, Pageable.ofSize(10));
        var guest = guestRespository.findById(guestId);

        if (guest.isPresent() && !treatments.isEmpty())
            treatmentService.includeGuestInTreatment(guest.get(), null);
        else {
            throw new TreatmentException("treatment does not exist");
        }
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean deleteGuest(UUID guestId) {
        guestRespository.deleteById(guestId);
        return Boolean.TRUE;
    }

    @Modifying
    public Boolean updateGuest(GuestPostDTO guestRequest) {

        var result = guestRespository.findById(guestRequest.id());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        guestRespository.save(null);

        return Boolean.TRUE;
    }

    public List<Guest> getAllGuests(UUID patientId) {
        return guestRespository.findAllByPatient_Id(patientId);
    }
}
