package ru.iate.gak.model;

import ru.iate.gak.domain.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timestamps")
public class TimestampEntity extends LongIdentifiableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diplom_id", referencedColumnName = "id")
    private DiplomEntity diplom;

    @Column(name = "timestamp_value")
    private LocalDateTime timestamp;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "point")
    private Status status;

    public DiplomEntity getDiplom() {
        return diplom;
    }

    public void setDiplom(DiplomEntity diplom) {
        this.diplom = diplom;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
