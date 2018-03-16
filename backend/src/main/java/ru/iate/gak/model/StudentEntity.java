package ru.iate.gak.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class StudentEntity extends LongIdentifiableEntity {

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_title")
    private GroupEntity group;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.REMOVE)
    private DiplomEntity diplom;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.REMOVE)
    private SpeakerEntity speaker;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public DiplomEntity getDiplom() {
        return diplom;
    }

    public void setDiplom(DiplomEntity diplom) {
        this.diplom = diplom;
    }

    public SpeakerEntity getSpeaker() {
        return speaker;
    }

    public void setSpeaker(SpeakerEntity speaker) {
        this.speaker = speaker;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }
}
