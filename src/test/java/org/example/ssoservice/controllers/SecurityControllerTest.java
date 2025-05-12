package org.example.ssoservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ssoservice.dtos.CredentialsDTO;
import org.example.ssoservice.dtos.TokenDTO;
import org.example.ssoservice.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для класса {@link SecurityController}.
 * Проверяют эндпоинты аутентификации и интроспекции JWT.
 */
@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityController securityController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        CredentialsDTO credentials = new CredentialsDTO("79001234567");
        TokenDTO tokenDTO = new TokenDTO("test.jwt.token");
        
        when(securityService.login(any(CredentialsDTO.class))).thenReturn(tokenDTO);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tokenDTO)));
    }

    @Test
    void introspect_withValidToken_returnsTokenInfo() throws Exception {
        TokenDTO tokenDTO = new TokenDTO("test.jwt.token");
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("active", true);
        tokenInfo.put("msisdn", "79001234567");
        tokenInfo.put("role", "ROLE_SUBSCRIBER");
        tokenInfo.put("ref_id", 123L);
        
        when(securityService.introspectJwt(any(TokenDTO.class))).thenReturn(tokenInfo);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();

        mockMvc.perform(post("/auth/introspection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tokenInfo)));
    }
}
