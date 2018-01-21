package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Group;
import ru.iate.gak.domain.Student;
import ru.iate.gak.domain.User;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.StudentEntity;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.DiplomRepository;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.repository.StudentRepository;
import ru.iate.gak.repository.UserRepository;
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

    @Autowired
    private DiplomRepository diplomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<Student> getStudentOfCurrentGroup(String group) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        List<Student> result = new ArrayList<>();
        studentRepository.getAllByGroup(groupEntity).forEach(s -> {
            Student student = new Student(s);
            student.setTitle(s.getDiplom().getTitle());
            student.setMentor(new User(s.getDiplom().getMentor()));
            student.setReviewer(new User(s.getDiplom().getReviewer()));
            result.add(student);
        });

        return result;
    }

    @Override
    @Transactional
    public void saveStudent(Student student) {
        GroupEntity groupEntity = groupRepository.findOne(student.getGroup().getTitle());
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + student.getGroup().getTitle() + "  не найдена");

        UserEntity mentorEntity = userRepository.findOne(student.getMentor().getId());
        if (mentorEntity == null) throw new RuntimeException("Руководитель с id " + student.getMentor().getId() + "  не найден");

        UserEntity reviewerEntity = userRepository.findOne(student.getReviewer().getId());
        if (reviewerEntity == null) throw new RuntimeException("Рецензент с id " + student.getReviewer().getId() + "  не найден");

        StudentEntity studentEntity = null;
        DiplomEntity diplomEntity = null;
        if (student.getId() != null) {
            studentEntity = studentRepository.findOne(student.getId());
            if (studentEntity != null && studentEntity.getDiplom() != null) {
                diplomEntity = studentEntity.getDiplom();
            } else throw new RuntimeException("Студент с id " + student.getId() + " не найден");
        } else {
            studentEntity = new StudentEntity();
            diplomEntity = new DiplomEntity();
        }

        studentEntity.setFirstname(student.getFirstname());
        studentEntity.setLastname(student.getLastname());
        studentEntity.setMiddlename(student.getMiddlename());
        studentEntity.setGroup(groupEntity);
        studentRepository.save(studentEntity);

        diplomEntity.setTitle(student.getTitle());
        diplomEntity.setMentor(mentorEntity);
        diplomEntity.setReviewer(reviewerEntity);
        diplomEntity.setStudent(studentEntity);
        diplomRepository.save(diplomEntity);
    }
}
