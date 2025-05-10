package org.example.ssoservice.utils;

import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class InitUtil implements CommandLineRunner{

    private final AppUserRepository appUserRepository;

    public InitUtil(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

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
