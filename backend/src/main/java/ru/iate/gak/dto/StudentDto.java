package ru.iate.gak.dto;

import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.Gender;
import ru.iate.gak.model.Status;
import ru.iate.gak.model.StudentEntity;

import java.time.ZoneOffset;

public class StudentDto extends LongIdentifiableDto {
    public String firstname;
    public String middlename;
    public String lastname;
    public Gender gender;
    public String title;
    public String executionPlace;
    public Long deletedTime;
    public GroupDto group;
    public UserDto mentor;
    public UserDto reviewer;
    public Status status;
    public byte[] report;
    public byte[] presentation;

    public StudentDto() {
    }

    public StudentDto(StudentEntity student) {
        super(student.getId());
        this.firstname = student.getFirstname();
        this.middlename = student.getMiddlename();
        this.lastname = student.getLastname();
        this.gender = student.getGender();
        this.deletedTime = (student.getDeletedTime() == null) ? null : student.getDeletedTime().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.group = student.getGroup() == null ? null : new GroupDto(student.getGroup());

        DiplomEntity diplomEntity = student.getDiplom();
        if (diplomEntity != null) {
            this.title = diplomEntity.getTitle();
            this.executionPlace = diplomEntity.getExecutionPlace();
            this.mentor = diplomEntity.getMentor() == null ? null : new UserDto(diplomEntity.getMentor());
            this.reviewer = diplomEntity.getReviewer() == null ? null : new UserDto(diplomEntity.getReviewer());
            this.status = diplomEntity.getStatus();
            this.report = diplomEntity.getReport() == null ? null : "".getBytes();
            this.presentation = diplomEntity.getPresentation() == null ? null : "".getBytes();
        }
    }
}
