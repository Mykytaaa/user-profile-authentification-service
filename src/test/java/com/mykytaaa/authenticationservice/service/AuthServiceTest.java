package com.mykytaaa.authenticationservice.service;

import com.mykytaaa.authenticationservice.config.AwsUserCognitoProperties;
import com.mykytaaa.authenticationservice.rest.dto.SignUpDto;
import com.mykytaaa.authenticationservice.rest.dto.SignUpConfirmationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private static final String EXAMPLE_EMAIL = "example@mail.com";
    private static final String CLIENT_ID = "clientId";
    private static final String SECRET_KEY = "secretKey";

    @Mock
    private CognitoIdentityProviderClient client;

    @Mock
    private AwsUserCognitoProperties properties;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldSignUpUser() {
        final SignUpDto signUpDto = new SignUpDto("testname", "testsurname", EXAMPLE_EMAIL, "examplepassword");
        when(properties.getClientId()).thenReturn(CLIENT_ID);
        when(properties.getSecretKey()).thenReturn(SECRET_KEY);

        authService.signUp(signUpDto);

        final ArgumentCaptor<SignUpRequest> argumentCaptor = ArgumentCaptor.forClass(SignUpRequest.class);
        verify(client, times(1)).signUp(argumentCaptor.capture());

        final SignUpRequest request = argumentCaptor.getValue();
        assertThat(request.username()).isEqualTo(signUpDto.email());
        assertThat(request.password()).isEqualTo(signUpDto.password());
        assertThat(request.clientId()).isEqualTo(CLIENT_ID);
    }

    @Test
    void shouldConfirmSignUp() {
        final SignUpConfirmationDto signUpConfirmationDto = new SignUpConfirmationDto(EXAMPLE_EMAIL, "123456");
        when(properties.getClientId()).thenReturn(CLIENT_ID);
        when(properties.getSecretKey()).thenReturn(SECRET_KEY);

        authService.confirmSignUp(signUpConfirmationDto);

        final ArgumentCaptor<ConfirmSignUpRequest> argumentCaptor = ArgumentCaptor.forClass(ConfirmSignUpRequest.class);
        verify(client, times(1)).confirmSignUp(argumentCaptor.capture());

        final ConfirmSignUpRequest request = argumentCaptor.getValue();
        assertThat(request.clientId()).isEqualTo(CLIENT_ID);
        assertThat(request.confirmationCode()).isEqualTo(signUpConfirmationDto.confirmationCode());
        assertThat(request.username()).isEqualTo(signUpConfirmationDto.email());
    }
}
