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
    public ResponseEntity<ErrorDetails> handlerBadRequest(RuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), LocalDateTime.now(), Boolean.FALSE);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OAuth2AuthenticationProcessingException.class})
    public ResponseEntity<ErrorDetails> handlerForbiddenRequest(RuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), LocalDateTime.now(), Boolean.FALSE);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
}
