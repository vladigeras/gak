package ru.iate.gak.dto;

import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.Status;

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

    public DiplomDto(DiplomEntity diplom) {
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
}
