package ru.iate.gak.dto;

import ru.iate.gak.model.CommissionEntity;

public class CommissionDto extends LongIdentifiableDto {

    public Integer listId;
    public UserDto user;

    public CommissionDto() {}

    public CommissionDto(CommissionEntity commission) {
        super(commission.getId());
        this.listId = commission.getListId();
        this.user = new UserDto(commission.getUser());
    }
}
