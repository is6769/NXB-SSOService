package org.example.ssoservice.utils;

import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Утилитарный класс для инициализации данных при запуске приложения.
 * Создает пользователя-менеджера по умолчанию, если он отсутствует.
 */
@Component
public class InitUtil implements CommandLineRunner{

    private final AppUserRepository appUserRepository;

    public InitUtil(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Выполняется при запуске приложения.
     * Проверяет наличие пользователей с ролью {@link AppRole#ROLE_MANAGER}.
     * Если менеджеры отсутствуют, создает нового пользователя-менеджера
     * с предопределенным MSISDN "-77777777777".
     *
     * @param args Аргументы командной строки (не используются).
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void run(String... args) {
        List<AppUser> managers = appUserRepository.findAllByRole(AppRole.ROLE_MANAGER);
        AppUser appUser = AppUser
                .builder()
                .msisdn("-77777777777")
                .role(AppRole.ROLE_MANAGER)
                .referenceId(null)
                .build();
        if (managers.isEmpty()) appUserRepository.save(appUser);

    }
}
