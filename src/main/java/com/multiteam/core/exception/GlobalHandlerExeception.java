package com.multiteam.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHandlerExeception extends ResponseEntityExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class, TreatmentException.class,
      ProfessionalException.class, ScheduleException.class})
  public ResponseEntity<ApiErrorResponse> handlerBadRequest(RuntimeException ex) {
    ApiErrorResponse errorDetails = new ApiErrorResponse.Builder(
        ex.getMessage(),
        ex.getStackTrace(),
        HttpStatus.BAD_REQUEST.value())
        .action(
            "400 Bad Request indica que o servidor não pode ou não irá processar a requisição devido a algum erro causado pelo cliente.")
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
  }

  @ExceptionHandler({OAuth2AuthenticationProcessingException.class, AccessDeniedException.class,
      BadCredentialsException.class})
  public ResponseEntity<ApiErrorResponse> handlerForbiddenRequest(RuntimeException ex) {
    ApiErrorResponse errorDetails = new ApiErrorResponse.Builder(
        ex.getMessage(),
        ex.getStackTrace(),
        HttpStatus.FORBIDDEN.value())
        .build();
    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handlerResourceNotFoundException(RuntimeException ex) {
    ApiErrorResponse errorDetails = new ApiErrorResponse.Builder(
        ex.getMessage(),
        ex.getStackTrace(),
        HttpStatus.NOT_FOUND.value())
        .build();
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }
}
