package org.example.ssoservice.services;

import io.jsonwebtoken.Claims;
import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.security.AppUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private AppUserDetails userDetails;
    private final Long referenceId = 123L;
    private final String msisdn = "79001234567";
    private final AppRole role = AppRole.ROLE_SUBSCRIBER;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        AppUser appUser = AppUser.builder()
                .id(1L)
                .msisdn(msisdn)
                .role(role)
                .referenceId(referenceId)
                .build();
                
        userDetails = new AppUserDetails(appUser);
    }

    @Test
    void generateJwt_shouldCreateValidToken() {
        String token = jwtService.generateJwt(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertTrue(jwtService.isNotExpired(token));
    }

    @Test
    void getClaims_shouldReturnCorrectClaims() {
        String token = jwtService.generateJwt(userDetails);

        Claims claims = jwtService.getClaims(token);

        assertEquals(msisdn, claims.get("msisdn"));
        assertEquals(role.name(), claims.get("role"));
        assertEquals(referenceId, ((Number) claims.get("ref_id")).longValue());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void isValid_withInvalidToken_shouldReturnFalse() {
        String invalidToken = "invalid.jwt.token";

        assertFalse(jwtService.isValid(invalidToken));
    }

    @Test
    void isNotExpired_withExpiredToken_shouldReturnFalse() {
        String token = jwtService.generateJwt(userDetails);

        assertTrue(jwtService.isNotExpired(token));
    }
}
