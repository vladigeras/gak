package ru.iate.gak.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
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

    @PostMapping(path = "/add")
    public boolean saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto.toUser());
        return true;
    }
}
