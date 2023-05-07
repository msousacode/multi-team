package com.multiteam.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/users",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_USER_READ')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        var result = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable("userId") UUID userId) {
        return userService.getUser(userId)
                .map(professional -> ResponseEntity.status(HttpStatus.OK).body(professional))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {

        if(userService.updateUser(userDTO)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{userId}/owner/{ownerId}")
    public ResponseEntity<?> inactiveUser(
            @PathVariable("userId") UUID userId,
            @PathVariable("ownerId") UUID ownerId) {

        if(userService.inactiveUser(userId, ownerId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
