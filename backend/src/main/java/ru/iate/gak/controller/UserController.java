package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.domain.Role;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.UserService;
import ru.iate.gak.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/get")
    @GakSecured(role = Roles.ADMIN)
    public List<UserDto> getUsers() {
        List<UserDto> result = new ArrayList<>();
        userService.getAllUsers().forEach(u -> {
            result.add(new UserDto(u));
        });
        return result;
    }

    @PostMapping(value = "/add", consumes = "application/json")
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

    @PostMapping(value = "/update", consumes = "application/json")
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

    @GetMapping(value = "/roles")
    @GakSecured(role = Roles.ADMIN)
    public List<String> getRoles() {
        List<String> result = new ArrayList<>();
        userService.getAllRoles().forEach(r -> {
            result.add(r.name());
        });
        return result;
    }

    @GetMapping(value = "/byRole")
    @GakSecured(role = Roles.ADMIN)
    public List<UserDto> getUsersByRole(@RequestParam(name = "role") String role) {
        List<UserDto> result = new ArrayList<>();
        userService.getAllUsersByRole(Role.valueOf(role)).forEach(u -> {
            result.add(new UserDto(u));
        });
        return result;
    }
}
