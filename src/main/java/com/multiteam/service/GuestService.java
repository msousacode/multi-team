package com.multiteam.service;

import com.multiteam.controller.dto.GuestDto;
import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.repository.GuestRespository;
import com.multiteam.service.util.ProvisinalPasswordUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRespository guestRespository;
    private final TreatamentService treatamentService;

    public GuestService(
            GuestRespository guestRespository,
            TreatamentService treatamentService) {
        this.guestRespository = guestRespository;
        this.treatamentService = treatamentService;
    }

    @Transactional
    public boolean createGuest(GuestDto guestDto) {

        var treatment = treatamentService.getAllTreatmentsByPatientId(guestDto.patientId());

        if (treatment.isEmpty())
            throw new RuntimeException();//Criar execption custom.

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

        treatamentService.includeGuestInTreatment(guest, treatment);

        return Boolean.TRUE;
    }
}
