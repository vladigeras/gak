package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.dto.GroupDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.GroupService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping(value = "/get")
    @GakSecured(roles = {Roles.ADMIN})
    public List<GroupDto> getGroups() {
        List<GroupDto> result = new ArrayList<>();
        groupService.getGroups().forEach(g -> result.add(new GroupDto(g)));
        return result;
    }

}