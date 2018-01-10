package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.service.UserService;
import ru.iate.gak.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/get")
    public List<UserDto> getUsers() {
        List<UserDto> result = new ArrayList<>();
        userService.getAllUsers().forEach(u -> {
            result.add(new UserDto(u));
        });
        return result;
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public void saveUser(@RequestBody UserDto userDto) {
        if (StringUtil.isStringNullOrEmptyTrim(userDto.firstname) || StringUtil.isStringNullOrEmptyTrim(userDto.lastname)
                || StringUtil.isStringNullOrEmptyTrim(userDto.login) || StringUtil.isStringNullOrEmptyTrim(userDto.password)) {
            throw new RuntimeException("Имя, фамилия, логин, пароль не могут быть пустыми");
        }
        if (userDto.roles == null || userDto.roles.isEmpty()) {
            throw new RuntimeException("Пользователь должен иметь хотя бы одну роль");
        }
        userService.saveUser(userDto.toUser());
    }

    @PostMapping(path = "/update", consumes = "application/json")
    public void updateUser(@RequestBody UserDto userDto) {
        if (StringUtil.isStringNullOrEmptyTrim(userDto.firstname) || StringUtil.isStringNullOrEmptyTrim(userDto.lastname)) {
            throw new RuntimeException("Имя, фамилия, логин не могут быть пустыми");
        }
        if (userDto.roles == null || userDto.roles.isEmpty()) {
            throw new RuntimeException("Пользователь должен иметь хотя бы одну роль");
        }
        userService.updateUser(userDto.toUser());
    }

    @GetMapping(path = "/roles")
    public List<String> getRoles() {
        List<String> result = new ArrayList<>();
        userService.getAllRoles().forEach(r -> {
            result.add(r.name());
        });
        return result;
    }
}
