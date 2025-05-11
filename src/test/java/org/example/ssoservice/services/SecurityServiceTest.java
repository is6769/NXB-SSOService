package org.example.ssoservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.example.ssoservice.dtos.CredentialsDTO;
import org.example.ssoservice.dtos.TokenDTO;
import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.example.ssoservice.security.AppUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private SecurityService securityService;

    private AppUser subscriber;
    private final String msisdn = "79001234567";
    private final String generatedToken = "test.jwt.token";

    @BeforeEach
    void setUp() {
        subscriber = AppUser.builder()
                .id(1L)
                .msisdn(msisdn)
                .role(AppRole.ROLE_SUBSCRIBER)
                .referenceId(123L)
                .build();
    }

    @Test
    void loadUserByUsername_withExistingUser_returnsUserDetails() {
        when(appUserRepository.findAppUserByMsisdn(msisdn)).thenReturn(Optional.of(subscriber));

        UserDetails result = securityService.loadUserByUsername(msisdn);

        assertNotNull(result);
        assertEquals(msisdn, result.getUsername());
        verify(appUserRepository).findAppUserByMsisdn(msisdn);
    }

    @Test
    void loadUserByUsername_withNonExistingUser_throwsException() {
        String nonExistingMsisdn = "79999999999";
        when(appUserRepository.findAppUserByMsisdn(nonExistingMsisdn)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> securityService.loadUserByUsername(nonExistingMsisdn));
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        CredentialsDTO credentials = new CredentialsDTO(msisdn);
        when(appUserRepository.findAppUserByMsisdn(msisdn)).thenReturn(Optional.of(subscriber));
        when(jwtService.generateJwt(any(AppUserDetails.class))).thenReturn(generatedToken);

        TokenDTO result = securityService.login(credentials);

        assertNotNull(result);
        assertEquals(generatedToken, result.token());
    }

    @Test
    void introspectJwt_withValidToken_returnsClaimsWithActiveTrue() {
        TokenDTO tokenDTO = new TokenDTO(generatedToken);
        Map<String, Object> claims = new HashMap<>();
        claims.put("msisdn", msisdn);
        claims.put("role", AppRole.ROLE_SUBSCRIBER.name());
        claims.put("ref_id", 123L);

        when(jwtService.isValid(generatedToken)).thenReturn(true);
        when(jwtService.isNotExpired(generatedToken)).thenReturn(true);
        when(jwtService.getClaims(generatedToken)).thenReturn(new DefaultClaims(claims));

        Map<String, Object> result = securityService.introspectJwt(tokenDTO);

        assertTrue((Boolean) result.get("active"));
        assertEquals(msisdn, result.get("msisdn"));
        assertEquals(AppRole.ROLE_SUBSCRIBER.name(), result.get("role"));
        assertEquals(123L, result.get("ref_id"));
    }

    @Test
    void introspectJwt_withInvalidToken_returnsActiveFalse() {
        TokenDTO tokenDTO = new TokenDTO("invalid.token");
        when(jwtService.isValid("invalid.token")).thenReturn(false);

        Map<String, Object> result = securityService.introspectJwt(tokenDTO);

        assertFalse((Boolean) result.get("active"));
        assertEquals(1, result.size());
    }

    @Test
    void introspectJwt_withExpiredToken_returnsActiveFalse() {
        TokenDTO tokenDTO = new TokenDTO(generatedToken);
        when(jwtService.isValid(generatedToken)).thenReturn(true);
        when(jwtService.isNotExpired(generatedToken)).thenReturn(false);

        Map<String, Object> result = securityService.introspectJwt(tokenDTO);

        assertFalse((Boolean) result.get("active"));
    }
}
