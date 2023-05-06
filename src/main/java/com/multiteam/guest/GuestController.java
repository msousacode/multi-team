package com.multiteam.guest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PostMapping
    public ResponseEntity<?> createGuest(@RequestBody GuestRequest guestRequest) {

        if (guestService.createGuest(guestRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PutMapping
    public ResponseEntity<?> updateGuest(@RequestBody GuestRequest guestRequest) {

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
