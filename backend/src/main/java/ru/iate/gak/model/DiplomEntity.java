package ru.iate.gak.model;

import javax.persistence.*;

@Entity
@Table(name = "diplom")
public class DiplomEntity extends LongIdentifiableEntity {

    @Column(name = "title")
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity reviewer;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public UserEntity getMentor() {
        return mentor;
    }

    public void setMentor(UserEntity mentor) {
        this.mentor = mentor;
    }

    public UserEntity getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserEntity reviewer) {
        this.reviewer = reviewer;
    }
}
