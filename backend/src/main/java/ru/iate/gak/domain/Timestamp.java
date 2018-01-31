package ru.iate.gak.domain;

import ru.iate.gak.model.TimestampEntity;

import java.time.LocalDateTime;

public class Timestamp extends LongIdentifiable {

    private LocalDateTime timestamp;
    private Diplom diplom;
    private Status status;

    public Timestamp() {}

    public Timestamp(TimestampEntity timestampEntity) {
        super(timestampEntity.getId());
        this.timestamp = timestampEntity.getTimestamp();
        this.diplom = new Diplom(timestampEntity.getDiplom());
        this.status = timestampEntity.getStatus();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Diplom getDiplom() {
        return diplom;
    }

    public void setDiplom(Diplom diplom) {
        this.diplom = diplom;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
