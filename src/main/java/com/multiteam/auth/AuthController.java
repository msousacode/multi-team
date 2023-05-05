package com.multiteam.auth;

import com.multiteam.auth.dto.AuthResponse;
import com.multiteam.auth.dto.LoginRequest;
import com.multiteam.auth.dto.SignUpRequest;
import com.multiteam.auth.dto.CheckTokenRequest;
import com.multiteam.auth.dto.CheckTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/v1/auth", produces = APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> authenticationUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new AuthResponse(authService.authenticationUser(loginRequest)));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> registerUser(@RequestBody SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
/*
    @PostMapping("/check-token")
    public ResponseEntity<CheckTokenResponse> checkToken(@RequestBody CheckTokenRequest checkTokenRequest) {
        var result = authService.checkToken(checkTokenRequest.token());

        if (result.isValid()) {
            return ResponseEntity.ok(new CheckTokenResponse(result.userId(), result.ownerId(), true));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CheckTokenResponse(null, null, false));
        }
    }*/
}