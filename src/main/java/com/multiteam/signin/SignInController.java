package com.multiteam.signin;

import com.multiteam.signin.dto.SignInDTO;
import com.multiteam.signin.dto.SignUpDTO;
import com.multiteam.signin.dto.TokenDTO;
import com.multiteam.signin.dto.TokenSigInDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/v1/auth", produces = APPLICATION_JSON_VALUE)
public class SignInController {

    private final SignInService signInService;

    private SignInController(SignInService authService) {
        this.signInService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenSigInDTO> signInUser(@RequestBody SignInDTO signInDTO) {
        return ResponseEntity.ok(new TokenSigInDTO(signInService.signInUser(signInDTO)));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpUser(@RequestBody SignUpDTO signUpRequest) {
        signInService.signUpUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/check-token")
    public ResponseEntity<TokenDTO> checkToken(@RequestBody TokenSigInDTO token) {
        return ResponseEntity.ok(signInService.checkToken(token.token()));
    }
}