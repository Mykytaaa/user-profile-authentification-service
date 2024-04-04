package com.mykytaaa.authenticationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsConfiguration {

    /**
     * Creates and configures an instance of CognitoIdentityProviderClient.
     *
     * @param region The AWS region used for configuration.
     * @return An instance of CognitoIdentityProviderClient.
     */
    @Bean
    public CognitoIdentityProviderClient identityProviderClient(@Value("${aws.region}") Region region) {
        return CognitoIdentityProviderClient.builder()
                .region(region)
                .build();
    }
}
