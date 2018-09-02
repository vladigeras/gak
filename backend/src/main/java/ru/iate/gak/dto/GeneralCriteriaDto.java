package ru.iate.gak.dto;

import ru.iate.gak.model.GeneralCriteriaEntity;

public class GeneralCriteriaDto extends LongIdentifiableDto {

    public Integer listId;
    public String title;

    public GeneralCriteriaDto() {}

    public GeneralCriteriaDto(GeneralCriteriaEntity generalCriteria) {
        super(generalCriteria.getId());
        this.listId = generalCriteria.getListId();
        this.title = generalCriteria.getTitle();
    }
}
