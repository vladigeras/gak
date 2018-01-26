package ru.iate.gak.domain;

import ru.iate.gak.model.SpeakerEntity;

import java.time.LocalDateTime;

public class Speaker extends LongIdentifiable {

    private Integer listId;
    private LocalDateTime date;
    private Student student;
    private Integer orderOfSpeaking;

    public Speaker() {}

    public Speaker(SpeakerEntity speakerEntity) {
        super(speakerEntity.getId());
        this.listId = speakerEntity.getListId();
        this.date = speakerEntity.getDate();
        this.student = new Student(speakerEntity.getStudent());
        this.orderOfSpeaking = speakerEntity.getOrderOfSpeaking();
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getOrderOfSpeaking() {
        return orderOfSpeaking;
    }

    public void setOrderOfSpeaking(Integer orderOfSpeaking) {
        this.orderOfSpeaking = orderOfSpeaking;
    }
}
