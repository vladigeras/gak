package ru.iate.gak.dto;

import ru.iate.gak.domain.Commission;

public class CommissionDto extends LongIdentifiableDto {

    public Integer listId;
    public UserDto user;

    public CommissionDto() {}

    public CommissionDto(Commission commission) {
        super(commission.getId());
        this.listId = commission.getListId();
        this.user = new UserDto(commission.getUser());
    }

    public Commission toCommission() {
        Commission commission = new Commission();
        commission.setId(this.id);
        commission.setListId(this.listId);
        commission.setUser(this.user.toUser());
        return commission;
    }
}
