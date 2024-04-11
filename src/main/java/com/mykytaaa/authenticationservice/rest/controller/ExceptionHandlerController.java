package com.mykytaaa.authenticationservice.rest.controller;

import com.mykytaaa.authenticationservice.rest.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles SecretHashException and returns an appropriate ResponseEntity with an ApiErrorDto.
     *
     * @param e The SecretHashException instance to handle.
     * @return ResponseEntity containing the appropriate HTTP status and an ApiErrorDto.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleSecretHashException(IllegalArgumentException e) {
        final ApiErrorDto response = new ApiErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
