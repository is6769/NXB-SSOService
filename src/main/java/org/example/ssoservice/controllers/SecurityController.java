package org.example.ssoservice.controllers;

import org.example.ssoservice.dtos.CredentialsDTO;
import org.example.ssoservice.dtos.TokenDTO;
import org.example.ssoservice.services.SecurityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер, отвечающий за обработку запросов, связанных с безопасностью:
 * аутентификация пользователей и интроспекция JWT-токенов.
 */
@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Обрабатывает запрос на вход пользователя в систему.
     *
     * @param credentialsDTO DTO с учетными данными пользователя (MSISDN).
     * @return {@link TokenDTO} с сгенерированным JWT-токеном.
     */
    @PostMapping("/login")
    public TokenDTO login(@RequestBody CredentialsDTO credentialsDTO){
        return securityService.login(credentialsDTO);
    }

    /**
     * Обрабатывает запрос на интроспекцию (проверку) JWT-токена.
     *
     * @param tokenDTO DTO с JWT-токеном для проверки.
     * @return Карта с результатами интроспекции (включая поле "active").
     */
    @PostMapping("/introspection")
    public Map<String,Object> introspectJwt(@RequestBody TokenDTO tokenDTO){
        return securityService.introspectJwt(tokenDTO);
    }
}
