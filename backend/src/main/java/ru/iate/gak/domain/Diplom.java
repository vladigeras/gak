package ru.iate.gak.domain;

import ru.iate.gak.model.DiplomEntity;

public class Diplom extends LongIdentifiable {

    private String title;
    private Student student;
    private User mentor;
    private User reviewer;
    private User consultant;
    private User ruleController;
    private Integer resultMark;
    private Status status;
    private String executionPlace;
    private byte[] report;
    private byte[] presentation;

    public Diplom() {}

    public Diplom(DiplomEntity diplomEntity) {
        super(diplomEntity.getId());
        this.title = diplomEntity.getTitle();
        this.student = new Student(diplomEntity.getStudent());
        this.mentor = new User(diplomEntity.getMentor());
        this.reviewer = new User(diplomEntity.getReviewer());
        this.consultant = new User(diplomEntity.getConsultant());
        this.ruleController = new User(diplomEntity.getRuleController());
        this.resultMark = diplomEntity.getResultMark();
        this.status = diplomEntity.getStatus();
        this.executionPlace = diplomEntity.getExecutionPlace();
        this.report = diplomEntity.getReport();
        this.presentation = diplomEntity.getPresentation();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
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

    public User getConsultant() {
        return consultant;
    }

    public void setConsultant(User consultant) {
        this.consultant = consultant;
    }

    public User getRuleController() {
        return ruleController;
    }

    public void setRuleController(User ruleController) {
        this.ruleController = ruleController;
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
}
