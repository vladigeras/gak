package ru.iate.gak.model;

import ru.iate.gak.domain.Status;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
public class StatusEntity {

    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "title")
    private Status status;

    public StatusEntity() {
    }

    public StatusEntity(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
