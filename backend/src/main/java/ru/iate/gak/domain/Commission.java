package ru.iate.gak.domain;

import ru.iate.gak.model.CommissionEntity;

public class Commission extends LongIdentifiable {

    private Integer listId;
    private User user;

    public Commission() {}

    public Commission(CommissionEntity commissionEntity) {
        super(commissionEntity.getId());
        this.listId = commissionEntity.getListId();
        this.user = new User(commissionEntity.getUser());
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
