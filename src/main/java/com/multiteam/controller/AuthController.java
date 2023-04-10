package com.multiteam.controller;

import com.multiteam.controller.dto.payload.ApiResponse;
import com.multiteam.controller.dto.payload.AuthResponse;
import com.multiteam.controller.dto.payload.LoginRequest;
import com.multiteam.controller.dto.payload.SignUpRequest;
import com.multiteam.service.AuthService;
import org.hibernate.mapping.Any;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/v1/auth", produces = APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticationUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new AuthResponse(authService.authenticationUser(loginRequest)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        var result = authService.registerUser(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }
}
