package com.mykytaaa.authenticationservice.rest.controller;

import com.mykytaaa.authenticationservice.rest.dto.SignUpDto;
import com.mykytaaa.authenticationservice.rest.dto.SignUpConfirmationDto;
import com.mykytaaa.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cognito")
@RequiredArgsConstructor
public class SignUpController {

    /**
     * The AuthService instance used for authentication operations.
     */
    private final AuthService authService;

    /**
     * Endpoint for signing up a user.
     *
     * @param request The SignUpDto containing user signup information.
     * @return ResponseEntity with the status code and the SignUpDto in the body.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDto> signUp(@RequestBody SignUpDto request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /**
     * Endpoint for confirming user email after signing up.
     *
     * @param request The UserConfirmationDto containing user confirmation information.
     * @return ResponseEntity with the status code and the UserConfirmationDto in the body.
     */
    @PostMapping("/sign-up/email-confirmation")
    public ResponseEntity<SignUpConfirmationDto> confirmUser(@RequestBody SignUpConfirmationDto request) {
        authService.confirmSignUp(request);
        return ResponseEntity.status(HttpStatus.OK).body(request);
    }
}
