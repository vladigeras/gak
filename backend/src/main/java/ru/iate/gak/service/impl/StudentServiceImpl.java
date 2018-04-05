package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        studentRepository.getAllByGroupAndDeletedTimeIsNull(groupEntity).forEach(s -> {
            Student student = new Student(s);
            student.setTitle(s.getDiplom().getTitle());
            student.setExecutionPlace(s.getDiplom().getExecutionPlace());
            student.setMentor(new User(s.getDiplom().getMentor()));
            student.setReviewer(new User(s.getDiplom().getReviewer()));
            student.setReport(s.getDiplom().getReport() == null ? null : "".getBytes());
            student.setPresentation(s.getDiplom().getPresentation() == null ? null : "".getBytes());
            result.add(student);
        });

        return result;
    }

    @Override
    @Transactional
    public Long saveStudent(Student student) {
        GroupEntity groupEntity = groupRepository.findOne(student.getGroup().getTitle());
        if (groupEntity == null)
            throw new RuntimeException("Группа с названием " + student.getGroup().getTitle() + "  не найдена");

        UserEntity mentorEntity = userRepository.findOne(student.getMentor().getId());
        if (mentorEntity == null)
            throw new RuntimeException("Руководитель с id " + student.getMentor().getId() + "  не найден");

        UserEntity reviewerEntity = userRepository.findOne(student.getReviewer().getId());
        if (reviewerEntity == null)
            throw new RuntimeException("Рецензент с id " + student.getReviewer().getId() + "  не найден");

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
        diplomEntity.setExecutionPlace(student.getExecutionPlace());
        diplomEntity.setMentor(mentorEntity);
        diplomEntity.setReviewer(reviewerEntity);
        diplomEntity.setReport(student.getReport());
        diplomEntity.setPresentation(student.getPresentation());
        diplomEntity.setStudent(studentEntity);
        diplomRepository.save(diplomEntity);

        return studentEntity.getId();
    }

    @Override
    @Transactional
    public void saveFiles(Long studentId, MultipartFile reportFile, MultipartFile presentationFile) throws IOException {
        StudentEntity studentEntity = studentRepository.findOne(studentId);
        if (studentEntity == null) throw new RuntimeException("Произошла ошибка");

        studentEntity.getDiplom().setReport(reportFile == null ? null : reportFile.getBytes());
        studentEntity.getDiplom().setPresentation(presentationFile == null ? null : presentationFile.getBytes());
        studentRepository.save(studentEntity);
    }

    @Override
    @Transactional
    public byte[] readFile(Long studentId, boolean isReport) {
        StudentEntity studentEntity = studentRepository.findOne(studentId);
        if (studentEntity == null) throw new RuntimeException("Произошла ошибка");

        if (isReport) {
            if (studentEntity.getDiplom().getReport() != null) {
                return studentEntity.getDiplom().getReport();
            } else throw new RuntimeException("У этого студента еще не загружен данный фаил");

        } else {
            if (studentEntity.getDiplom().getPresentation() != null) {
                return studentEntity.getDiplom().getPresentation();
            } else throw new RuntimeException("У этого студента еще не загружен данный фаил");
        }
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        StudentEntity student = studentRepository.findOne(id);
        if (student == null) throw new RuntimeException("Произошла ошибка");

        student.setDeletedTime(LocalDateTime.now(ZoneOffset.UTC));
        studentRepository.save(student);
    }
}
