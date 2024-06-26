package com.multiteam.modules.role;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
    path = "/v1/roles",
    consumes = APPLICATION_JSON_VALUE
)
@RestController
public class RoleController {

  private final RoleRepository roleRepository;

  public RoleController(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @GetMapping
  public ResponseEntity<List<Role>> getAllRoles() {
    return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findAll());
  }
}
