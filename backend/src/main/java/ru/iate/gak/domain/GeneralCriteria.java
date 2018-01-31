package ru.iate.gak.domain;

import ru.iate.gak.model.GeneralCriteriaEntity;

public class GeneralCriteria extends LongIdentifiable {

    private Integer listId;
    private String title;

    public GeneralCriteria() {}

    public GeneralCriteria(GeneralCriteriaEntity generalCriteriaEntity) {
        super(generalCriteriaEntity.getId());
        this.listId = generalCriteriaEntity.getListId();
        this.title = generalCriteriaEntity.getTitle();
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
