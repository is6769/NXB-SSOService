package org.example.ssoservice.dtos;

public record ValidIntrospectionDTO(
        Boolean active,
        Long id,
        String role
) {
}
