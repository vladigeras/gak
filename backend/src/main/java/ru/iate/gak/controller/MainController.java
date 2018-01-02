package ru.iate.gak.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@Api(value = "mainController", description = "Test description of main controller")
public class MainController {

    @GetMapping(path = "/hello")
    public String backendTest() {
        return "hello from backend part!";
    }
}
