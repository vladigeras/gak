package ru.iate.gak.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.service.UserService;

@RestController
@RequestMapping(value = "/api")
@Api(value = "mainController", description = "Test description of main controller")
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/hello")
    public String backendTest() {
        userService.getAllUsers();
        return "OK";
    }
}
