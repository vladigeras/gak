package ru.iate.gak.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Value("${security.authTokenSecret}")
    private String secret;

    @Override
    public String generateTokenFromString(String str) {
        return Jwts.builder()
                .setSubject(str)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public String parseStringFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
