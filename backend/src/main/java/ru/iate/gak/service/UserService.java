package ru.iate.gak.service;

import ru.iate.gak.domain.User;
import ru.iate.gak.domain.Role;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    List<User> getAllUsersByRole(Role role);
    void saveUser(User user);
    void updateUser(User user);
    List<Role> getAllRoles();
}
