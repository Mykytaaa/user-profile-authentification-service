package com.mykytaaa.authenticationservice.service;

import com.mykytaaa.authenticationservice.config.AwsUserCognitoProperties;
import com.mykytaaa.authenticationservice.rest.dto.SignUpDto;
import com.mykytaaa.authenticationservice.rest.dto.SignUpConfirmationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * The algorithm used for HMAC-SHA256 hashing.
     */
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    /**
     * The client for interacting with Amazon Cognito Identity Provider.
     */
    private final CognitoIdentityProviderClient identityProviderClient;

    /**
     * The AWS properties containing necessary configuration.
     */
    private final AwsUserCognitoProperties properties;

    /**
     * Signs up a user using the provided SignUpDto.
     *
     * @param requestDto The SignUpDto containing user signup information.
     */
    public void signUp(SignUpDto requestDto) {
        final AttributeType attributeTypeEmail = AttributeType.builder()
                .name("email")
                .value(requestDto.email())
                .build();

        final AttributeType attributeTypeName = AttributeType.builder()
                .name("name")
                .value(requestDto.name())
                .build();

        final AttributeType attributeTypeSurname = AttributeType.builder()
                .name("family_name")
                .value(requestDto.surname())
                .build();


        final String secretVal = calculateSecretHash(properties.getClientId(),
                properties.getSecretKey(),
                requestDto.email());
        final SignUpRequest request = SignUpRequest.builder()
                .userAttributes(List.of(attributeTypeEmail, attributeTypeName, attributeTypeSurname))
                .username(requestDto.email())
                .password(requestDto.password())
                .clientId(properties.getClientId())
                .secretHash(secretVal)
                .build();

        identityProviderClient.signUp(request);
    }

    /**
     * Confirms user signup using the provided UserConfirmationDto.
     *
     * @param confirmation The UserConfirmationDto containing user confirmation information.
     */
    public void confirmSignUp(SignUpConfirmationDto confirmation) {
        final ConfirmSignUpRequest req = ConfirmSignUpRequest.builder()
                .clientId(properties.getClientId())
                .confirmationCode(confirmation.confirmationCode())
                .username(confirmation.email())
                .secretHash(calculateSecretHash(properties.getClientId(),
                        properties.getSecretKey(),
                        confirmation.email()))
                .build();


        identityProviderClient.confirmSignUp(req);
    }

    /**
     * Calculates the secret hash for the provided user pool client ID, client secret, and email.
     *
     * @param userPoolClientId     The user pool client ID.
     * @param userPoolClientSecret The user pool client secret.
     * @param email                The user's email.
     * @return The calculated secret hash.
     * @throws IllegalArgumentException If an error occurs during secret hash calculation.
     */
    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String email) {
        final SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);

        final Mac mac;
        try {
            mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        mac.update(email.getBytes(StandardCharsets.UTF_8));
        final byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(rawHmac);
    }
}
