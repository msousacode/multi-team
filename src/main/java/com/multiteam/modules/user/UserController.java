package com.multiteam.modules.user;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.modules.role.Role;

import java.security.Provider;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping(
    path = "/v1/users",
    produces = APPLICATION_JSON_VALUE,
    consumes = APPLICATION_JSON_VALUE
)
@RestController
public class UserController {

  private final UserService userService;

  public UserController(final UserService userService) {
    this.userService = userService;
  }


  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PutMapping
  public ResponseEntity<?> updateUser(@RequestBody final UserDTO userDTO) {
    if (userService.updateUser(userDTO)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
      return ResponseEntity.status(
              HttpStatus.OK).body(userService.createUser(userDTO, AuthProviderEnum.local)
      );
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL', 'SUPERVISOR')")
  @GetMapping
  public ResponseEntity<Page<UserDTO>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(value = "sort", defaultValue = "name") String sort,
      @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.getAllUsers(
            PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'SUPERVISOR')")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUser(@PathVariable("userId") final UUID userId) {
    return userService.getUser(userId)
        .map(professional -> ResponseEntity.status(HttpStatus.OK).body(professional))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> inactiveUser(@PathVariable("userId") final UUID userId) {
    if (userService.inactiveUser(userId)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PutMapping("/reset")
  public ResponseEntity<?> resetPassword(@RequestBody final PasswordResetDTO passwordResetDTO) {
    if (!passwordResetDTO.password1().equals(passwordResetDTO.password2())) {
      return ResponseEntity.badRequest().build();
    } else {
      userService.resetPassword(passwordResetDTO);
      return ResponseEntity.ok().build();
    }
  }

  @GetMapping("/{userId}/roles")
  public ResponseEntity<List<Role>> getRolesPermissions(@PathVariable("userId") final UUID userId) {
    return ResponseEntity.ok().body(userService.getRolesPermissions(userId));
  }
}
