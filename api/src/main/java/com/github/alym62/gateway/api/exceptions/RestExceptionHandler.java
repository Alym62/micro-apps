package com.github.alym62.gateway.api.exceptions;

import com.github.alym62.gateway.api.helpers.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDetails> handlerBusinessException(BusinessException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Business exception")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .details(exception.getMessage())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
