package ru.iate.gak.dto;

import ru.iate.gak.model.SpeakerEntity;

import java.time.ZoneOffset;

public class SpeakerDto extends LongIdentifiableDto {

    public Integer listId;
    public Long date;
    public StudentDto student;
    public Integer orderOfSpeaking;

    public SpeakerDto() {}

    public SpeakerDto(SpeakerEntity speaker) {
        super(speaker.getId());
        this.listId = speaker.getListId();
        this.date = speaker.getDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.student = new StudentDto(speaker.getStudent());
        this.orderOfSpeaking = speaker.getOrderOfSpeaking();
    }
}
