package ru.iate.gak.model;

import javax.persistence.*;

@Entity
@Table(name = "criteria")
public class CriteriaEntity extends LongIdentifiableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diplom_id", referencedColumnName = "id")
    private DiplomEntity diplom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    public DiplomEntity getDiplom() {
        return diplom;
    }

    public void setDiplom(DiplomEntity diplom) {
        this.diplom = diplom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
