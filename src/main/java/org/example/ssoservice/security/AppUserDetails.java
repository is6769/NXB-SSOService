package org.example.ssoservice.security;

import org.example.ssoservice.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса {@link UserDetails} для Spring Security.
 * Представляет аутентифицированного пользователя в системе.
 */
public class AppUserDetails implements UserDetails {

    private final AppUser appUser;

    public AppUserDetails(AppUser appUser) {
        this.appUser = appUser;
    }

    /**
     * Возвращает коллекцию прав (ролей) пользователя.
     * @return Список, содержащий роль пользователя в виде {@link SimpleGrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(appUser.getRole().name()));
    }

    /**
     * Возвращает роль пользователя в виде строки.
     * @return Имя роли пользователя.
     */
    public String getRole(){
        return appUser.getRole().name();
    }

    /**
     * Возвращает пароль пользователя. В данном приложении пароли не используются,
     * поэтому метод возвращает {@code null}.
     * @return {@code null}.
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * Возвращает референсный ID пользователя.
     * Это может быть ID абонента из другой системы или {@code null} для менеджеров.
     * @return Референсный ID пользователя.
     */
    public Long getReferenceId(){
        return appUser.getReferenceId();
    }

    /**
     * Возвращает имя пользователя (MSISDN).
     * @return MSISDN пользователя.
     */
    @Override
    public String getUsername() {
        return appUser.getMsisdn();
    }
}
