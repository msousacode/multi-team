package com.multiteam.service;

import com.multiteam.controller.dto.GuestDto;
import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.repository.GuestRespository;
import com.multiteam.service.util.ProvisinalPasswordUtil;
import com.multiteam.vo.DataResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
public class GuestService {

    private final GuestRespository guestRespository;
    private final TreatmentService treatmentService;
    private final CredentialService credentialService;

    public GuestService(
            GuestRespository guestRespository,
            TreatmentService treatmentService,
            CredentialService credentialService) {
        this.guestRespository = guestRespository;
        this.treatmentService = treatmentService;
        this.credentialService = credentialService;
    }

    @Transactional
    public DataResponse createGuest(GuestDto guestDto) {

        var treatment = treatmentService.findAllTreatmentsByPatientId(guestDto.patientId());

        if (treatment.isEmpty())
            return new DataResponse(null, "treatment not found, try again", false);

        if (credentialService.isThereCredential(guestDto.email())) {
            return new DataResponse(null, "guest registration already exists", false);
        } else {
            var credential = new Credential(guestDto.email(), ProvisinalPasswordUtil.generate());

            var builder = new Guest.Builder(
                    null,
                    guestDto.name(),
                    guestDto.middleName(),
                    guestDto.relationship(),
                    guestDto.cellPhone(),
                    guestDto.email(),
                    true,
                    credential)
                    .build();

            var guest = guestRespository.save(builder);

            treatmentService.includeGuestInTreatment(guest, treatment);
        }
        return new DataResponse(guestDto.email(), "guest registration with success", true);
    }

    @Transactional
    public Boolean addGuestInTreatment(UUID patientId, UUID guestId) {

        var treatments = treatmentService.findAllTreatmentsByPatientId(patientId);
        var guest = guestRespository.findById(guestId);

        if (guest.isPresent() && !treatments.isEmpty())
            treatmentService.includeGuestInTreatment(guest.get(), treatments);
        else {
            throw new RuntimeException("");//custom
        }
        return Boolean.TRUE;
    }

    //exclude guest. Roles: admin, controller

    //edit patient. Roles: admin, controller

    //get all guests filtering by patientId. Roles: professional, admin, controller
}
