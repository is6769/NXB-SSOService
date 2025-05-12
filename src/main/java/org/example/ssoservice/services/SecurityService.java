package org.example.ssoservice.services;

import org.example.ssoservice.dtos.CredentialsDTO;
import org.example.ssoservice.dtos.TokenDTO;
import org.example.ssoservice.repositories.AppUserRepository;
import org.example.ssoservice.security.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис безопасности, отвечающий за аутентификацию пользователей,
 * генерацию и интроспекцию JWT-токенов.
 * Реализует {@link UserDetailsService} для интеграции со Spring Security.
 */
@Service
public class SecurityService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public SecurityService(AppUserRepository appUserRepository, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    /**
     * Выполняет вход пользователя в систему.
     * Загружает данные пользователя по MSISDN и генерирует JWT-токен.
     *
     * @param credentialsDTO DTO с учетными данными пользователя (MSISDN).
     * @return {@link TokenDTO} с сгенерированным JWT-токеном.
     * @throws UsernameNotFoundException если пользователь с указанным MSISDN не найден.
     */
    public TokenDTO login(CredentialsDTO credentialsDTO) {
        AppUserDetails appUserDetails = (AppUserDetails) loadUserByUsername(credentialsDTO.msisdn());
        return new TokenDTO(jwtService.generateJwt(appUserDetails));
    }

    /**
     * Загружает данные пользователя по его имени пользователя (MSISDN).
     * Используется Spring Security для аутентификации.
     *
     * @param username Имя пользователя (MSISDN).
     * @return {@link UserDetails} объект с данными пользователя.
     * @throws UsernameNotFoundException если пользователь с указанным MSISDN не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(appUserRepository.findAppUserByMsisdn(username).orElseThrow(()->new UsernameNotFoundException("No such msisdn present in database.")));
    }

    /**
     * Выполняет интроспекцию (проверку) JWT-токена.
     * Проверяет валидность и срок действия токена.
     *
     * @param tokenDTO DTO с JWT-токеном для проверки.
     * @return Карта с результатами интроспекции. Содержит поле "active" (true/false)
     *         и другие клеймы токена, если он валиден.
     */
    public Map<String,Object> introspectJwt(TokenDTO tokenDTO) {
        String token = tokenDTO.token();
        Map<String,Object> claims;
        if (jwtService.isValid(token) && jwtService.isNotExpired(token)){
            claims = new HashMap<>(jwtService.getClaims(token));
            claims.put("active",true);
        } else {
            claims = Map.of("active",false);
        }
        return claims;

    }
}
