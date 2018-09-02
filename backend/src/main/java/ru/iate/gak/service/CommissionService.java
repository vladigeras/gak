package ru.iate.gak.service;

import ru.iate.gak.dto.CommissionDto;
import ru.iate.gak.model.CommissionEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface CommissionService {
    List<CommissionEntity> getCommissionsByListId(Integer listId);

    void setPresidentRoleTemporally(CommissionDto commission);

    void doTaskForDeleteRoleWhenDateExpired(CommissionDto commission, Date date);

    default Date getRoleExpiredTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();           //get 6 a.m. next day by MSK +3.00
    }
}
