package ru.iate.gak.service;

import org.springframework.web.multipart.MultipartFile;
import ru.iate.gak.domain.Student;

import java.io.IOException;
import java.util.List;

public interface StudentService {
    List<Student> getStudentOfCurrentGroup(String group);
    Long saveStudent(Student student);
    void saveFiles(Long id, MultipartFile reportFile, MultipartFile presentationFile) throws IOException;
}
