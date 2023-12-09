package com.multiteam.modules.guest;

import com.multiteam.core.enums.RelationshipEnum;
import com.multiteam.modules.guest.dto.GuestDTO;
import com.multiteam.modules.guest.dto.GuestPostDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/guests",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<?>> getAllGuests(@PathVariable("patientId") UUID patientId) {
        var guests = guestService.getAllGuests(patientId).stream().map(guest -> new GuestDTO(guest.getName(), RelationshipEnum.getValue(guest.getRelationship()).name(), guest.isActive())).toList();
        return ResponseEntity.ok(guests);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> createGuest(@RequestBody GuestPostDTO guestPostDTO, @PathVariable UUID patientId) {
        return guestService.createGuest(guestPostDTO, patientId) ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PutMapping
    public ResponseEntity<?> updateGuest(@RequestBody GuestPostDTO guestRequest) {

        if (guestService.updateGuest(guestRequest)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @DeleteMapping("/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable("guestId") UUID guestId) {

        if (guestService.deleteGuest(guestId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
