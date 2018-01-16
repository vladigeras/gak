package ru.iate.gak.dto;

import ru.iate.gak.domain.Student;

public class StudentDto extends LongIdentifiableDto {
    public String firstname;
    public String middlename;
    public String lastname;

    public StudentDto() {}

    public StudentDto(Student student) {
        super(student.getId());
        this.firstname = student.getFirstname();
        this.middlename = student.getMiddlename();
        this.lastname = student.getLastname();
    }

    public Student toStudent() {
        Student student = new Student();
        student.setFirstname(this.firstname);
        student.setMiddlename(this.middlename);
        student.setLastname(this.lastname);
        return student;
    }
}
