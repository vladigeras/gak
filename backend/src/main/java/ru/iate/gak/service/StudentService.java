package ru.iate.gak.service;

import org.springframework.web.multipart.MultipartFile;
import ru.iate.gak.dto.StudentDto;
import ru.iate.gak.model.StudentEntity;

import java.io.IOException;
import java.util.List;

public interface StudentService {
    List<StudentEntity> getStudentOfCurrentGroup(String group);

    Long saveStudent(StudentDto student);
    void saveFiles(Long studentId, MultipartFile reportFile, MultipartFile presentationFile) throws IOException;
    byte[] readFile(Long studentId, boolean isReport);
    void deleteStudent(Long id);
}
