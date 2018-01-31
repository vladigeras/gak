package ru.iate.gak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "general_criteria")
public class GeneralCriteriaEntity extends LongIdentifiableEntity {

    @Column(name = "list_id")
    private Integer listId;

    @Column(name = "title")
    private String title;

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
