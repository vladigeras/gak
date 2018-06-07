package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.CommissionDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.CommissionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/commissions")
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    @GetMapping(value = "/byListId")
    @GakSecured(roles = {Roles.SECRETARY, Roles.PRESIDENT})
    public List<CommissionDto> getCommissionsByListId(@RequestParam(name = "listId") Integer listId) {
        if (listId == null) throw new RuntimeException("Укажите номер списка");
        return commissionService.getCommissionsByListId(listId).stream().map(CommissionDto::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/transferPresidentRole", consumes = "application/json")
    @GakSecured(roles = {Roles.SECRETARY, Roles.PRESIDENT})
    public void transferPresidentRole(@RequestBody CommissionDto commissionDto) {
        if (commissionDto.id == null) throw new RuntimeException("Не выбран член комиссии");
        commissionService.setPresidentRoleTemporally(commissionDto.toCommission());
        commissionService.doTaskForDeleteRoleWhenDateExpired(commissionDto.toCommission(), commissionService.getRoleExpiredTime());
    }

}
