package ru.iate.gak.dto;

import org.springframework.web.multipart.MultipartFile;
import ru.iate.gak.domain.Student;

import java.io.IOException;

public class StudentDto extends LongIdentifiableDto {
    public String firstname;
    public String middlename;
    public String lastname;
    public String title;
    public GroupDto group;
    public UserDto mentor;
    public UserDto reviewer;
    public byte[] report;
    public byte[] presentation;

    public StudentDto() {}

    public StudentDto(Student student) {
        super(student.getId());
        this.firstname = student.getFirstname();
        this.middlename = student.getMiddlename();
        this.lastname = student.getLastname();
        this.title = student.getTitle();
        this.group = student.getGroup() == null ? null : new GroupDto(student.getGroup());
        this.mentor = student.getMentor() == null ? null : new UserDto(student.getMentor());
        this.reviewer = student.getReviewer() == null ? null : new UserDto(student.getReviewer());
    }

    public Student toStudent() {
        Student student = new Student();
        student.setId(this.id);
        student.setFirstname(this.firstname);
        student.setMiddlename(this.middlename);
        student.setLastname(this.lastname);
        student.setTitle(this.title);
        student.setGroup(this.group.toGroup());
        student.setMentor(this.mentor.toUser());
        student.setReviewer(this.reviewer.toUser());
        student.setReport(this.report);
        student.setPresentation(this.presentation);
        return student;
    }
}
