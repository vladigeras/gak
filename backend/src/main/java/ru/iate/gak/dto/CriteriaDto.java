package ru.iate.gak.dto;

import ru.iate.gak.domain.Criteria;

public class CriteriaDto extends LongIdentifiableDto {

    public DiplomDto diplom;
    public CommissionDto commissionDto;
    public String title;
    public Integer rating;
    public String comment;

    public CriteriaDto() {}

    public CriteriaDto(Criteria criteria) {
        super(criteria.getId());
        this.diplom = new DiplomDto(criteria.getDiplom());
        this.commissionDto = new CommissionDto(criteria.getCommission());
        this.title = criteria.getTitle();
        this.rating = criteria.getRating();
        this.comment = criteria.getComment();
    }

    public Criteria toCriteria() {
        Criteria criteria = new Criteria();
        criteria.setId(this.id);
        criteria.setDiplom((this.diplom == null) ? null : this.diplom.toDiplom());
        criteria.setCommission((this.commissionDto == null) ? null : this.commissionDto.toCommission());
        criteria.setTitle(this.title);
        criteria.setRating(this.rating);
        criteria.setComment(this.comment);
        return criteria;
    }
}
