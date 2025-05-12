package org.example.ssoservice.dtos;

/**
 * DTO (Data Transfer Object) для передачи учетных данных пользователя при входе в систему.
 *
 * @param msisdn MSISDN (номер телефона) пользователя.
 */
public record CredentialsDTO(
        String msisdn
) {
}
