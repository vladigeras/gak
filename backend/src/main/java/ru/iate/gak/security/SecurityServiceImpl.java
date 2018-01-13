package ru.iate.gak.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication auth(String login, String password) {
        try {

            //Достаем юзера
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);

            //Проверяем пароль
            String passwordHash = userDetails.getPassword();

            if (password == null
                    || !passwordEncoder.matches(password, passwordHash)) {
                return null;
            }

            //Возвращаем объект аутентификации
            return new CustomAuthentication(userDetails);

        } catch (UsernameNotFoundException e) {
            return null;
        }
    }
}
