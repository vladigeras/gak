package ru.iate.gak.security;

import org.springframework.security.core.Authentication;

public interface SecurityService {

    Authentication auth(String login, String password);
}
