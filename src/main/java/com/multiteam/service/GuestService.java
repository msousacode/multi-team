package com.multiteam.service;

import com.multiteam.controller.dto.GuestDto;
import com.multiteam.exception.TreatmentNotExistsException;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.repository.GuestRespository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.multiteam.constants.ApplicationErrorsEnum.TREATMENT_DOES_NOT_EXIST;

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
    public Boolean createGuest(GuestDto guestDto) {

        var treatment = treatmentService.getAllTreatmentsByPatientId(guestDto.patientId());

        if (treatment.isEmpty()) {
            return Boolean.FALSE;
        }

        if (credentialService.checkIfCredentialExists(guestDto.email())) {
            return Boolean.FALSE;
        } else {
            //var credential = new Credential(guestDto.email(), ProvisinalPasswordUtils.generate());

            var builder = new Guest.Builder(
                    null,
                    guestDto.name(),
                    guestDto.middleName(),
                    guestDto.relationship(),
                    guestDto.cellPhone(),
                    guestDto.email(),
                    true)
                    .build();

            var guest = guestRespository.save(builder);

            treatmentService.includeGuestInTreatment(guest, treatment);
        }
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean addGuestInTreatment(UUID patientId, UUID guestId) {

        var treatments = treatmentService.getAllTreatmentsByPatientId(patientId);
        var guest = guestRespository.findById(guestId);

        if (guest.isPresent() && !treatments.isEmpty())
            treatmentService.includeGuestInTreatment(guest.get(), treatments);
        else {
            throw new TreatmentNotExistsException(TREATMENT_DOES_NOT_EXIST.name());//custom
        }
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean deleteGuest(UUID guestId) {
        guestRespository.deleteById(guestId);
        return Boolean.TRUE;
    }

    @Modifying
    public Boolean updateGuest(Guest guest) {

        var result = guestRespository.findById(guest.getId());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Guest.Builder(
                result.get().getId(),
                guest.getName(),
                guest.getMiddleName(),
                guest.getRelationship(),
                guest.getCellPhone(),
                guest.getEmail(),
                guest.isActive())
                .build();

        guestRespository.save(builder);

        return Boolean.TRUE;
    }
}
