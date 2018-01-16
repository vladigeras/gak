package ru.iate.gak.domain;

import ru.iate.gak.model.StudentEntity;

public class Student extends LongIdentifiable {
    private String firstname;
    private String middlename;
    private String lastname;

    public Student() {

    }

    public Student(StudentEntity studentEntity) {
        super(studentEntity.getId());
        this.firstname = studentEntity.getFirstname();
        this.middlename = studentEntity.getMiddlename();
        this.lastname = studentEntity.getLastname();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
