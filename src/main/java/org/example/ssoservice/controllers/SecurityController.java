package org.example.ssoservice.controllers;

import io.jsonwebtoken.Claims;
import org.example.ssoservice.dtos.CredentialsDTO;
import org.example.ssoservice.dtos.TokenDTO;
import org.example.ssoservice.services.SecurityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody CredentialsDTO credentialsDTO){
        return securityService.login(credentialsDTO);
    }

    @PostMapping("/introspection")
    public Map<String,Object> introspectJwt(@RequestBody TokenDTO tokenDTO){
        return securityService.introspectJwt(tokenDTO);
    }
}
