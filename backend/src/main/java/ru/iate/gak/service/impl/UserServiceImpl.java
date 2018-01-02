package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.User;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(u -> {
            result.add(new User(u));
        });
        return result;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());
        userEntity.setFirstname(user.getFirstname());
        userEntity.setMiddlename(user.getMiddlename());
        userEntity.setLastname(user.getLastname());
        userEntity.setRoles(user.getRoles());
        userRepository.save(userEntity);
    }
}
