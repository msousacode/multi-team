package com.multiteam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalHandlerExeception extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, TreatmentNotExistsException.class})
    public ResponseEntity<ApiErrorResponse> handlerBadRequest(RuntimeException ex) {
        ApiErrorResponse errorDetails = new ApiErrorResponse.Builder(
                ex.getMessage(), ex.getCause(), ex.getStackTrace(), ex.getLocalizedMessage()).action("400 Bad Request indica que o servidor não pode ou não irá processar a requisição devido a alguma coisa que foi entendida como um erro do cliente.").build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OAuth2AuthenticationProcessingException.class})
    public ResponseEntity<ApiErrorResponse> handlerForbiddenRequest(RuntimeException ex) {
        ApiErrorResponse errorDetails = new ApiErrorResponse.Builder(ex.getMessage(), ex.getCause(), ex.getStackTrace(), ex.getLocalizedMessage()).build();
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
}
