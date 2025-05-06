package org.example.ssoservice.dtos;

public record NewSubscriberDTO(
        Long subscriberId,
        String msisdn
) {
}
