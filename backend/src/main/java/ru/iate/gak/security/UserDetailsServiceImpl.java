package ru.iate.gak.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            logger.error("С логином " + login + " был использован неверный пароль");
            throw new UsernameNotFoundException(login);
        }
        return new UserPrincipal(user);
    }
}
