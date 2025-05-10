package org.example.ssoservice.repositories;

import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findAppUserByMsisdn(String msisdn);

    List<AppUser> findAllByRole(AppRole role);
}
