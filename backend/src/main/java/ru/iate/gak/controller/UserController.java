package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.domain.Role;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.UserService;
import ru.iate.gak.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    @GakSecured(role = Roles.ADMIN)
    public List<UserDto> getUsers() {
        return userService.getAllUsers().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/", consumes = "application/json")
    @GakSecured(role = Roles.ADMIN)
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

    @PutMapping(value = "/", consumes = "application/json")
    @GakSecured(role = Roles.ADMIN)
    public void updateUser(@RequestBody UserDto userDto) {
        if (StringUtil.isStringNullOrEmptyTrim(userDto.firstname) || StringUtil.isStringNullOrEmptyTrim(userDto.lastname)) {
            throw new RuntimeException("Имя, фамилия, логин не могут быть пустыми");
        }
        if (userDto.roles == null || userDto.roles.isEmpty()) {
            throw new RuntimeException("Пользователь должен иметь хотя бы одну роль");
        }
        userService.updateUser(userDto.toUser());
    }

    @GetMapping(value = "/roles/all")
    @GakSecured(role = Roles.ADMIN)
    public List<String> getRoles() {
        return userService.getAllRoles().stream().map(Enum::name).collect(Collectors.toList());
    }

    @GetMapping(value = "/roles")
    @GakSecured(role = Roles.ADMIN)
    public List<UserDto> getUsersByRole(@RequestParam(name = "role") String role) {
        return userService.getAllUsersByRole(Role.valueOf(role)).stream().map(UserDto::new).collect(Collectors.toList());
    }
}
