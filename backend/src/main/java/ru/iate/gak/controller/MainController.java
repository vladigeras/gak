package ru.iate.gak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class MainController {

    @GetMapping(path = "/hello")
    public String backendTest() {
        return "hello from backend part!";
    }
}
