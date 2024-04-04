package com.mykytaaa.authenticationservice.rest.dto;

public record SignUpDto(String name,
                        String surname,
                        String email,
                        String password) {
}
