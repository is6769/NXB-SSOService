package org.example.ssoservice.services;

import io.jsonwebtoken.Claims;
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

@Service
public class SecurityService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public SecurityService(AppUserRepository appUserRepository, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public TokenDTO login(CredentialsDTO credentialsDTO) {
        AppUserDetails appUserDetails = (AppUserDetails) loadUserByUsername(credentialsDTO.msisdn());
        return new TokenDTO(jwtService.generateJwt(appUserDetails));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(appUserRepository.findAppUserByMsisdn(username).orElseThrow(()->new UsernameNotFoundException("No such msisdn present in database.")));
    }

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
