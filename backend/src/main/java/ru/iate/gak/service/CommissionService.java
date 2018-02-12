package ru.iate.gak.service;

import ru.iate.gak.domain.Commission;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface CommissionService {
    List<Commission> getCommissionsByListId(Integer listId);
    void setPresidentRoleTemporally(Commission commission);
    void doTaskForDeleteRoleWhenDateExpired(Commission commission, Date date);

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
