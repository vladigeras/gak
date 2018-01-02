package ru.iate.gak.service;

import ru.iate.gak.domain.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
}
