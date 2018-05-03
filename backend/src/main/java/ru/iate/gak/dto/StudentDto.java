package ru.iate.gak.dto;

import ru.iate.gak.domain.Gender;
import ru.iate.gak.domain.Status;
import ru.iate.gak.domain.Student;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class StudentDto extends LongIdentifiableDto {
    public String firstname;
    public String middlename;
    public String lastname;
    public Gender gender;
    public String title;
    public String executionPlace;
    public Long deleteTime;
    public GroupDto group;
    public UserDto mentor;
    public UserDto reviewer;
    public Status status;
    public byte[] report;
    public byte[] presentation;

    public StudentDto() {
    }

    public StudentDto(Student student) {
        super(student.getId());
        this.firstname = student.getFirstname();
        this.middlename = student.getMiddlename();
        this.lastname = student.getLastname();
        this.gender = student.getGender();
        this.title = student.getTitle();
        this.executionPlace = student.getExecutionPlace();
        this.deleteTime = (student.getDeleteTime() == null) ? null : student.getDeleteTime().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.group = student.getGroup() == null ? null : new GroupDto(student.getGroup());
        this.mentor = student.getMentor() == null ? null : new UserDto(student.getMentor());
        this.reviewer = student.getReviewer() == null ? null : new UserDto(student.getReviewer());
        this.report = student.getReport() == null ? null : "".getBytes();
        this.presentation = student.getPresentation() == null ? null : "".getBytes();
        this.status = student.getStatus();

    }

    public Student toStudent() {
        Student student = new Student();
        student.setId(this.id);
        student.setFirstname(this.firstname);
        student.setMiddlename(this.middlename);
        student.setLastname(this.lastname);
        student.setGender(this.gender);
        student.setTitle(this.title);
        student.setExecutionPlace(this.executionPlace);
        student.setDeleteTime((this.deleteTime == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(this.deleteTime), ZoneOffset.UTC));
        student.setGroup((this.group == null) ? null : this.group.toGroup());
        student.setMentor((this.mentor == null) ? null : this.mentor.toUser());
        student.setReviewer((this.reviewer == null) ? null : this.reviewer.toUser());
        student.setStatus(this.status);
        student.setReport(this.report);
        student.setPresentation(this.presentation);
        return student;
    }
}
