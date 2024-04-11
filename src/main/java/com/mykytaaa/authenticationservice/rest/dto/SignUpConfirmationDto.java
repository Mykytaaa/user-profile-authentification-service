package com.mykytaaa.authenticationservice.rest.dto;

public record SignUpConfirmationDto(String email,
                                    String confirmationCode) {
}
