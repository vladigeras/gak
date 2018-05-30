package ru.iate.gak.model;

import ru.iate.gak.domain.Status;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "diplom")
public class DiplomEntity extends LongIdentifiableEntity {

    @Column(name = "title")
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity reviewer;

    @Column(name = "result_mark")
    private Integer resultMark;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "execution_place")
    private String executionPlace;

    @Column(name = "report")
    private byte[] report;

    @Column(name = "presentation")
    private byte[] presentation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diplom")
    private Set<TimestampEntity> timestamps;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diplom")
    private Set<QuestionEntity> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diplom")
    private Set<CriteriaEntity> criteria;

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

    public byte[] getReport() {
        return report;
    }

    public void setReport(byte[] report) {
        this.report = report;
    }

    public byte[] getPresentation() {
        return presentation;
    }

    public void setPresentation(byte[] presentation) {
        this.presentation = presentation;
    }

    public Integer getResultMark() {
        return resultMark;
    }

    public void setResultMark(Integer resultMark) {
        this.resultMark = resultMark;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExecutionPlace() {
        return executionPlace;
    }

    public void setExecutionPlace(String executionPlace) {
        this.executionPlace = executionPlace;
    }

    public Set<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionEntity> questions) {
        this.questions = questions;
    }
}
