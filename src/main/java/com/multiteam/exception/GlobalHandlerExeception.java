package com.multiteam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalHandlerExeception {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorDetails> handlerAsBadRequest(RuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), LocalDateTime.now(), Boolean.FALSE);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
