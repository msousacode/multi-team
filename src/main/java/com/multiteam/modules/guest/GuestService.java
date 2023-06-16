package com.multiteam.modules.guest;


import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.exception.TreatmentException;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.user.User;
import com.multiteam.modules.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class GuestService {

    private final GuestRespository guestRespository;
    private final TreatmentService treatmentService;
    private final UserRepository userRepository;

    public GuestService(
            GuestRespository guestRespository,
            TreatmentService treatmentService,
            UserRepository userRepository) {
        this.guestRespository = guestRespository;
        this.treatmentService = treatmentService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createGuest(GuestRequest guestRequest) {

        var treatment = treatmentService.getAllTreatments(null, Pageable.ofSize(10));

        if(guestRequest.ownerId() == null) {
            //throw new OwnerException("value ownerId cannot be null");
        }

        if (treatment.isEmpty()) {
            return Boolean.FALSE;
        } else {
            var user = new User.Builder(null, null, guestRequest.name(), guestRequest.email(), true).provider(AuthProviderEnum.local).build();

            var userResult = userRepository.save(user);

            var builder = new Guest.Builder(
                    null,
                    guestRequest.name(),
                    guestRequest.middleName(),
                    guestRequest.relationship(),
                    guestRequest.cellPhone(),
                    guestRequest.email(),
                    true,
                    userResult)
                    .build();

            var guest = guestRespository.save(builder);

            treatmentService.includeGuestInTreatment(guest, null);
        }
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
    public Boolean updateGuest(GuestRequest guestRequest) {

        var result = guestRespository.findById(guestRequest.id());

        if (result.isEmpty()) {
            return Boolean.FALSE;
        }

        var builder = new Guest.Builder(
                result.get().getId(),
                guestRequest.name(),
                guestRequest.middleName(),
                guestRequest.relationship(),
                guestRequest.cellPhone(),
                guestRequest.email(),
                guestRequest.active(),
                result.get().getUser())
                .build();

        guestRespository.save(builder);

        return Boolean.TRUE;
    }
}
