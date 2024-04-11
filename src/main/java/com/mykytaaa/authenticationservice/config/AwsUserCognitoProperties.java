package com.mykytaaa.authenticationservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AwsUserCognitoProperties {

    /**
     * The AWS client ID used for authentication.
     */
    @Value("${aws.client-id}")
    private String clientId;

    /**
     * The secret key associated with the AWS client ID.
     */
    @Value("${aws.secret-key}")
    private String secretKey;
}
