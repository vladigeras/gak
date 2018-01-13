package ru.iate.gak.security;

public interface AuthTokenService {

    String generateTokenFromString(String str);

    String parseStringFromToken(String token);
}
