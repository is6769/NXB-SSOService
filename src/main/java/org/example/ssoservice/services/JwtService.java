package org.example.ssoservice.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final MacAlgorithm algorithm=Jwts.SIG.HS512;
    private final SecretKey secret=algorithm.key().build();

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token){
        return
                Jwts.parser()
                        .verifyWith(secret)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
    }

    public String generateJwt(AppUserDetails userDetails){
        return
                Jwts.builder()
                    .claims()
                        .issuedAt(new Date())
                        .add("username",userDetails.getUsername())
                        .add("email", userDetails.getEmail())
                        .add("roles",userDetails.getRolesAsStrings())
                    .and()
                    .signWith(secret, algorithm)
                    .compact();
    }
}
