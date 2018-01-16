package ru.iate.gak.service;

import ru.iate.gak.domain.Group;
import ru.iate.gak.domain.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudentOfCurrentGroup(String group);
}
