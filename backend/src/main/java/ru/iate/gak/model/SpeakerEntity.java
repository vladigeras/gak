package ru.iate.gak.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "speakers")
public class SpeakerEntity extends LongIdentifiableEntity {

    @Column(name = "list_id")
    private Integer listId;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private StudentEntity student;

    @Column(name = "order_of_speaking")
    private Integer orderOfSpeaking;

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

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public Integer getOrderOfSpeaking() {
        return orderOfSpeaking;
    }

    public void setOrderOfSpeaking(Integer orderOfSpeaking) {
        this.orderOfSpeaking = orderOfSpeaking;
    }
}
