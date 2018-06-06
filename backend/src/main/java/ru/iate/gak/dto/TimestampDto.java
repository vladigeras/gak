package ru.iate.gak.dto;

import ru.iate.gak.domain.Status;
import ru.iate.gak.domain.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimestampDto extends LongIdentifiableDto {

    public Long timestamp;
    public DiplomDto diplom;
    public Status status;

    public TimestampDto() {}

    public TimestampDto(Timestamp timestamp) {
        super(timestamp.getId());
        this.status = timestamp.getStatus();
        this.diplom = new DiplomDto(timestamp.getDiplom());
        this.timestamp = timestamp.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public Timestamp toTimestamp() {
        Timestamp timestamp = new Timestamp();
        timestamp.setDiplom((this.diplom == null) ? null : this.diplom.toDiplom());
        timestamp.setStatus((this.status == null)? null : this.status );
        timestamp.setTimestamp((this.timestamp == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), ZoneOffset.UTC));
        return timestamp;
    }
}
