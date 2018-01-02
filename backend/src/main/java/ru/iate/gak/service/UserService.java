package ru.iate.gak.service;

import ru.iate.gak.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();
}
