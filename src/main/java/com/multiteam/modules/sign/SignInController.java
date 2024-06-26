package com.multiteam.modules.sign;

import com.multiteam.modules.sign.payload.ForgotDTO;
import com.multiteam.modules.sign.payload.TokenSigInDTO;
import com.multiteam.modules.sign.payload.SignInDTO;
import com.multiteam.modules.sign.payload.SignUpDTO;
import com.multiteam.modules.sign.payload.TokenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/v1/auth", produces = APPLICATION_JSON_VALUE)
public final class SignInController {

  private final SignInService signInService;

  private SignInController(final SignInService authService) {
    this.signInService = authService;
  }

  @PostMapping("/sign-in")
  public ResponseEntity<TokenSigInDTO> signInUser(@RequestBody final SignInDTO signInDTO) {
    return ResponseEntity.ok(new TokenSigInDTO(signInService.signInUser(signInDTO)));
  }

  @PostMapping("/sign-up")
  public ResponseEntity<Void> signUpUser(@RequestBody final SignUpDTO signUpRequest) {
    signInService.signUpUser(signUpRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/info-token")
  public ResponseEntity<TokenDTO> infoToken(@RequestBody final TokenSigInDTO token) {
    return ResponseEntity.ok(signInService.infoToken(token.token()));
  }

  @PostMapping("/validate-token")
  public ResponseEntity<Boolean> validateToken(@RequestBody final TokenSigInDTO token) {
    return ResponseEntity.ok(signInService.validateToken(token.token()));
  }

  @PostMapping("/forgot")
  public ResponseEntity<?> forgot(@RequestBody final ForgotDTO forgotDTO) {
    return ResponseEntity.ok(signInService.forget(forgotDTO.email()));
  }
}