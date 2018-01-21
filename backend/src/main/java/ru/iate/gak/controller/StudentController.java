package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/save", consumes = "application/json")
    @GakSecured(roles = {Roles.ADMIN})
    public void saveStudent(@RequestBody StudentDto studentDto) {
        if (StringUtil.isStringNullOrEmptyTrim(studentDto.firstname) || StringUtil.isStringNullOrEmptyTrim(studentDto.lastname)
                || StringUtil.isStringNullOrEmptyTrim(studentDto.title)) {
            throw new RuntimeException("Имя, фамилия, тема работы не могут быть пустыми");
        }

        if (studentDto.group == null || StringUtil.isStringNullOrEmptyTrim(studentDto.group.title))
            throw new RuntimeException("Не выбрана группа");
        if (studentDto.mentor == null || studentDto.reviewer == null || studentDto.mentor.id == null || studentDto.reviewer.id == null)
            throw new RuntimeException("Выберите руководителя и рецензента");

        studentService.saveStudent(studentDto.toStudent());
    }
}
