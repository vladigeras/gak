package ru.iate.gak.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<StudentEntity> students;

    public GroupEntity() {}
    public GroupEntity(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentEntity> students) {
        this.students = students;
    }
}
