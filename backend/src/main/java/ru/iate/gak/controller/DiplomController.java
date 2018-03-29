package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.iate.gak.dto.DiplomDto;
import ru.iate.gak.service.DiplomService;

@RestController
@RequestMapping(value = "/diploms")
public class DiplomController {

    @Autowired
    private DiplomService diplomService;

    @GetMapping(value = "/bySpeaker")
    public DiplomDto getDiplomById(@RequestParam(name = "id") Long speakerId) {
        if (speakerId > 0) {
            return new DiplomDto(diplomService.getDiplomBySpeakerId(speakerId));
        } else throw new RuntimeException("Неверное значение id");
    }
}
