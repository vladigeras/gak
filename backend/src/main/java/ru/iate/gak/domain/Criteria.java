package ru.iate.gak.domain;

import ru.iate.gak.model.CriteriaEntity;

public class Criteria extends LongIdentifiable {

    private Diplom diplom;
    private User user;
    private String title;
    private Integer rating;
    private String comment;

    public Criteria() {}

    public Criteria(CriteriaEntity criteriaEntity) {
        super(criteriaEntity.getId());
        this.diplom = new Diplom(criteriaEntity.getDiplom());
        this.user = new User(criteriaEntity.getUser());
        this.title = criteriaEntity.getTitle();
        this.rating = criteriaEntity.getRating();
        this.comment = criteriaEntity.getComment();
    }

    public Diplom getDiplom() {
        return diplom;
    }

    public void setDiplom(Diplom diplom) {
        this.diplom = diplom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
