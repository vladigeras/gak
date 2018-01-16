package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.dto.StudentDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.StudentService;
import ru.iate.gak.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/ofGroup")
    @GakSecured(roles = {Roles.ADMIN})
    public List<StudentDto> getStudentsOfGroup(@RequestParam(value = "group") String group) {

        if (StringUtil.isStringNullOrEmptyTrim(group)) throw new RuntimeException("Неверное значение для группы");

        List<StudentDto> result = new ArrayList<>();
        studentService.getStudentOfCurrentGroup(group).forEach(s -> {
            result.add(new StudentDto(s));
        });
        return result;
    }

}
