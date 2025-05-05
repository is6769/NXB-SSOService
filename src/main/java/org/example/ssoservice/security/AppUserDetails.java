package org.example.ssoservice.security;

import org.example.ssoservice.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


public class AppUserDetails implements UserDetails {

    private final AppUser appUser;

    public AppUserDetails(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(appUser.getRole().name()));
    }

    public String getRole(){
        return appUser.getRole().name();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getReferenceId(){
        return appUser.getReferenceId();
    }

    @Override
    public String getUsername() {
        return appUser.getMsisdn();
    }
}
