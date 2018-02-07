package ru.iate.gak.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    private String secret = SecurityConstants.AUTH_TOKEN_SECRET;

    @Override
    public String generateTokenFromString(String str) {
        return Jwts.builder()
                .setSubject(str)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME*1000))   //*1000 because we need milliseconds
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
