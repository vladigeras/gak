package ru.iate.gak.service;

import ru.iate.gak.dto.UserDto;
import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();

    List<UserEntity> getAllUsersByRole(Role role);

    void saveUser(UserDto user);

    void updateUser(UserDto user);
    List<Role> getAllRoles();
}
