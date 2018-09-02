package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.UserService;
import ru.iate.gak.util.StringUtil;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public List<UserEntity> getAllUsersByRole(Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return userRepository.findAllByRoles(roles);
    }

    @Override
    @Transactional
    public void saveUser(UserDto user) {
        if (userRepository.findByLogin(user.login) != null)
            throw new RuntimeException("Данный логин уже используется");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.id);
        userEntity.setLogin(user.login);
        userEntity.setPassword(passwordEncoder.encode(user.password));
        userEntity.setFirstname(user.firstname);
        userEntity.setGender(user.gender);
        userEntity.setMiddlename(user.middlename);
        userEntity.setLastname(user.lastname);
        userEntity.setRoles(user.roles);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUser(UserDto user) {
        UserEntity userEntity = userRepository.findByLogin(user.login);
        if (userEntity == null) throw new RuntimeException("Произошла ошибка");     //логин сменить нельзя

        if (!StringUtil.isStringNullOrEmptyTrim(user.password)) {
            userEntity.setPassword(passwordEncoder.encode(user.password));
        }
        userEntity.setFirstname(user.firstname);
        userEntity.setMiddlename(user.middlename);
        userEntity.setLastname(user.lastname);
        userEntity.setGender(user.gender);
        userEntity.setRoles(user.roles);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return new ArrayList<>(Arrays.asList(Role.values()));
    }
}
