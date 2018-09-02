package ru.iate.gak.dto;

import ru.iate.gak.model.Status;
import ru.iate.gak.model.TimestampEntity;

import java.time.ZoneOffset;

public class TimestampDto extends LongIdentifiableDto {

    public Long timestamp;
    public DiplomDto diplom;
    public Status status;

    public TimestampDto() {}

    public TimestampDto(TimestampEntity timestamp) {
        super(timestamp.getId());
        this.status = timestamp.getStatus();
        this.diplom = timestamp.getDiplom() == null ? null : new DiplomDto(timestamp.getDiplom());
        this.timestamp = timestamp.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
