package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<UserEntity> getAllUsers() {
        userRepository.findAll().forEach(System.out::println);
        return null;
    }
}
