package ru.iate.gak.service;

import ru.iate.gak.domain.User;
import ru.iate.gak.domain.Role;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    void updateUser(User user);
    List<Role> getAllRoles();
}
