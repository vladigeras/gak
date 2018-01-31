package ru.iate.gak.dto;

import ru.iate.gak.domain.Criteria;

public class CriteriaDto extends LongIdentifiableDto {

    public DiplomDto diplom;
    public UserDto user;
    public String title;
    public Integer rating;
    public String comment;

    public CriteriaDto() {}

    public CriteriaDto(Criteria criteria) {
        super(criteria.getId());
        this.diplom = new DiplomDto(criteria.getDiplom());
        this.user = new UserDto(criteria.getUser());
        this.title = criteria.getTitle();
        this.rating = criteria.getRating();
        this.comment = criteria.getComment();
    }

    public Criteria toCriteria() {
        Criteria criteria = new Criteria();
        criteria.setId(this.id);
        criteria.setDiplom(this.diplom.toDiplom());
        criteria.setUser(this.user.toUser());
        criteria.setTitle(this.title);
        criteria.setRating(this.rating);
        criteria.setComment(this.comment);
        return criteria;
    }
}
