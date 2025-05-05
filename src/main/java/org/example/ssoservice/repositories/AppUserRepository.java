package org.example.ssoservice.repositories;

import org.example.ssoservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findAppUserByMsisdn(String msisdn);
}
