package ru.iate.gak.dto;

import ru.iate.gak.domain.GeneralCriteria;

public class GeneralCriteriaDto extends LongIdentifiableDto {

    public Integer listId;
    public String title;

    public GeneralCriteriaDto() {}

    public GeneralCriteriaDto(GeneralCriteria generalCriteria) {
        super(generalCriteria.getId());
        this.listId = generalCriteria.getListId();
        this.title = generalCriteria.getTitle();
    }

    public GeneralCriteria toGeneralCriteria() {
        GeneralCriteria generalCriteria = new GeneralCriteria();
        generalCriteria.setId(this.id);
        generalCriteria.setListId(this.listId);
        generalCriteria.setTitle(this.title);
        return generalCriteria;
    }
}
