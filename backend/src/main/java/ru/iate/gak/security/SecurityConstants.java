package ru.iate.gak.security;

public class SecurityConstants {
    public static final String SECRET = "isb14iate";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "gak2018 ";
    public static final String HEADER_STRING = "Authorization";
    public static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
}
