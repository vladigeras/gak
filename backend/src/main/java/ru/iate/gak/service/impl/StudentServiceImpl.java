package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Student;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.repository.StudentRepository;
import ru.iate.gak.service.StudentService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    @Transactional
    public List<Student> getStudentOfCurrentGroup(String group) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        List<Student> result = new ArrayList<>();
        studentRepository.getAllByGroup(groupEntity).forEach(s -> {
            result.add(new Student(s));
        });

        return result;
    }
}
