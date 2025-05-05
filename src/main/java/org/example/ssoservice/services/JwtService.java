package org.example.ssoservice.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.example.ssoservice.security.AppUserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    public boolean isNotExpired(String token){
        Claims claims = getClaims(token);
        return claims.getExpiration().after(Date.from(Instant.now()));
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
        Date issuedAt = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 1 hour expiration
        return
                Jwts.builder()
                    .claims()
                        .issuedAt(issuedAt)
                        .expiration(expiration)
                        .add("msisdn",userDetails.getUsername())
                        .add("role",userDetails.getRole())
                        .add("ref_id",userDetails.getReferenceId())
                    .and()
                    .signWith(secret, algorithm)
                    .compact();
    }
}
