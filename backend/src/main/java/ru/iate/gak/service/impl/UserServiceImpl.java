package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Role;
import ru.iate.gak.domain.User;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.RoleRepository;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.UserService;
import ru.iate.gak.util.StringUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public List<User> getAllUsersByRole(Role role) {
        List<User> result = new ArrayList<>();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userRepository.findAllByRoles(roles).forEach(u -> {
            result.add(new User(u));
        });
        return result;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (userRepository.findByLogin(user.getLogin()) != null)
            throw new RuntimeException("Данный логин уже используется");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setFirstname(user.getFirstname());
        userEntity.setMiddlename(user.getMiddlename());
        userEntity.setLastname(user.getLastname());
        userEntity.setRoles(user.getRoles());
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        UserEntity userEntity = userRepository.findByLogin(user.getLogin());
        if (userEntity == null) throw new RuntimeException("Произошла ошибка");     //логин сменить нельзя

        if (!StringUtil.isStringNullOrEmptyTrim(user.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userEntity.setFirstname(user.getFirstname());
        userEntity.setMiddlename(user.getMiddlename());
        userEntity.setLastname(user.getLastname());
        userEntity.setRoles(user.getRoles());
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        List<Role> result = new ArrayList<>();
        roleRepository.findAll().forEach(r -> {
            result.add(r.getRole());
        });
        return result;
    }
}
