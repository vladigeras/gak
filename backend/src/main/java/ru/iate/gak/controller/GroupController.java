package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.dto.GroupDto;
import ru.iate.gak.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping(value = "/")
    public List<GroupDto> getGroups() {
        return groupService.getGroups().stream().map(GroupDto::new).collect(Collectors.toList());

    }

}
