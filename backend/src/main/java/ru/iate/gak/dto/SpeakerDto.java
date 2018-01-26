package ru.iate.gak.dto;

import ru.iate.gak.domain.Speaker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SpeakerDto extends LongIdentifiableDto {

    public Integer listId;
    public Long date;
    public StudentDto student;
    public Integer orderOfSpeaking;

    public SpeakerDto() {}

    public SpeakerDto(Speaker speaker) {
        super(speaker.getId());
        this.listId = speaker.getListId();
        this.date = speaker.getDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.student = new StudentDto(speaker.getStudent());
        this.orderOfSpeaking = speaker.getOrderOfSpeaking();
    }

    public Speaker toSpeaker() {
        Speaker speaker = new Speaker();
        speaker.setId(this.id);
        speaker.setListId(this.listId);
        speaker.setDate((this.date == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(this.date), ZoneOffset.UTC));
        speaker.setStudent(this.student.toStudent());
        speaker.setOrderOfSpeaking(this.orderOfSpeaking);
        return speaker;
    }
}
