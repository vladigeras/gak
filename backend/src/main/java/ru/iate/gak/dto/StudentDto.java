package ru.iate.gak.dto;

import ru.iate.gak.domain.Student;

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
        this.report = student.getReport() == null ? null : "".getBytes();
        this.presentation = student.getPresentation() == null ? null : "".getBytes();
    }

    public Student toStudent() {
        Student student = new Student();
        student.setId(this.id);
        student.setFirstname(this.firstname);
        student.setMiddlename(this.middlename);
        student.setLastname(this.lastname);
        student.setTitle(this.title);
        student.setGroup((this.group == null) ? null : this.group.toGroup());
        student.setMentor((this.mentor == null) ? null : this.mentor.toUser());
        student.setReviewer((this.reviewer == null) ? null : this.reviewer.toUser());
        student.setReport(this.report);
        student.setPresentation(this.presentation);
        return student;
    }
}
