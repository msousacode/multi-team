package com.multiteam.service;

import com.multiteam.controller.dto.GuestDto;
import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.repository.GuestRespository;
import com.multiteam.service.util.ProvisinalPasswordUtil;
import com.multiteam.controller.dto.ResponseDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public ResponseDto createGuest(GuestDto guestDto) {

        var treatment = treatmentService.findAllTreatmentsByPatientId(guestDto.patientId());

        if (treatment.isEmpty())
            return new ResponseDto(null, "treatment not found, try again", false);

        if (credentialService.isThereCredential(guestDto.email())) {
            return new ResponseDto(null, "guest registration already exists", false);
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
        return new ResponseDto(guestDto.email(), "guest registration with success", true);
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

    @Transactional
    public ResponseDto removeGuest(UUID guestId) {
        guestRespository.deleteById(guestId);
        return new ResponseDto(null, "guest removed successfully", true);
    }

    @Modifying
    public ResponseDto editGuest(Guest guest) {

        var result = guestRespository.findById(guest.getId());

        if (result.isEmpty()) {
            return new ResponseDto(null, "resource not found", false);
        }

        var builder = new Guest.Builder(
                result.get().getId(),
                guest.getName(),
                guest.getMiddleName(),
                guest.getRelationship(),
                guest.getCellPhone(),
                guest.getEmail(),
                guest.isActive(),
                result.get().getCredential())
                .build();

        guestRespository.save(builder);

        return new ResponseDto(null, "guest updated successfully", true);
    }
}
