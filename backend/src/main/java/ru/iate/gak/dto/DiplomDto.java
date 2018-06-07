package ru.iate.gak.dto;

import ru.iate.gak.domain.Diplom;
import ru.iate.gak.domain.Status;

public class DiplomDto extends LongIdentifiableDto {

    public String title;
    public StudentDto student;
    public UserDto mentor;
    public UserDto reviewer;
    public Integer resultMark;
    public Status status;
    public String executionPlace;
    public byte[] report;
    public byte[] presentation;

    public DiplomDto() {}

    public DiplomDto(Diplom diplom) {
        super(diplom.getId());
        this.title = diplom.getTitle();
        this.student = (diplom.getStudent() == null) ? null : new StudentDto(diplom.getStudent());
        this.mentor = (diplom.getMentor() == null) ? null : new UserDto(diplom.getMentor());
        this.reviewer = (diplom.getReviewer() == null) ? null : new UserDto(diplom.getReviewer());
        this.resultMark = diplom.getResultMark();
        this.status = diplom.getStatus();
        this.executionPlace = diplom.getExecutionPlace();
        this.report = diplom.getReport() == null ? null : "".getBytes();
        this.presentation = diplom.getPresentation() == null ? null : "".getBytes();
    }

    public Diplom toDiplom() {
        Diplom diplom = new Diplom();
        diplom.setId(this.id);
        diplom.setTitle(this.title);
        diplom.setStudent(this.student.toStudent());
        diplom.setMentor(this.mentor.toUser());
        diplom.setReviewer(this.reviewer.toUser());
        diplom.setResultMark(this.resultMark);
        diplom.setStatus(this.status);
        diplom.setExecutionPlace(this.executionPlace);
        diplom.setReport(this.report);
        diplom.setPresentation(this.presentation);
        return diplom;
    }
}
