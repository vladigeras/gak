package ru.iate.gak.model;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class StudentEntity extends LongIdentifiableEntity {

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "lastname")
    private String lastname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_title")
    private GroupEntity group;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student")
    private DiplomEntity diplom;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student")
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
}
