package org.example.ssoservice.dtos;

/**
 * DTO (Data Transfer Object) для передачи JWT-токена.
 *
 * @param token Строковое представление JWT-токена.
 */
public record TokenDTO(
        String token
) {
}
