package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.dto.GeneralCriteriaDto;
import ru.iate.gak.service.CriteriaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/criteria")
public class CriteriaController {

    @Autowired
    private CriteriaService criteriaService;

    @GetMapping(value = "/getDefault")
    public List<GeneralCriteriaDto> getDefaultCriteriaList(@RequestParam(name = "listId", defaultValue = "1") String listId) {
        try {
            return criteriaService.getDefaultCriteria(Integer.valueOf(listId)).stream().map(GeneralCriteriaDto::new).collect(Collectors.toList());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Неверное значение параметра. Введите число");
        }
    }
}
