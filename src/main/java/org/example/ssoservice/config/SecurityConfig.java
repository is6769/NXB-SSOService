package org.example.ssoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности Spring Security для SSO-сервиса.
 * Определяет правила доступа к эндпоинтам.
 */
@Configuration
public class SecurityConfig {

    /**
     * Определяет цепочку фильтров безопасности.
     * В данной конфигурации отключается CSRF-защита, CORS, форма входа и базовая аутентификация.
     * Все запросы разрешены без аутентификации, так как аутентификация и авторизация
     * обрабатываются через эндпоинты {@code /auth/login} и {@code /auth/introspection}.
     *
     * @param http Конфигуратор {@link HttpSecurity}.
     * @return Сконфигурированный {@link SecurityFilterChain}.
     * @throws Exception если возникает ошибка при конфигурации.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(c ->{
            c
                    .anyRequest().permitAll();

        });

        return http.build();
    }
}
