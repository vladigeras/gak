package ru.iate.gak.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "commissions")
public class CommissionEntity extends LongIdentifiableEntity {

    @Column(name = "list_id")
    private Integer listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commission")
    private List<CriteriaEntity> criteriaCreatedByCommission;

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
