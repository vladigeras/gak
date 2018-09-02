package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.TimestampDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.TimestampService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/timestamps")
public class TimestampController {

    @Autowired
    private TimestampService timestampService;

    @PostMapping(value = "/", consumes = "application/json")
    @GakSecured(roles = {Roles.PRESIDENT})
    public void saveTimestampList(@RequestParam(name = "speakerId", required = true) Long id,
                                  @RequestBody List<TimestampDto> timestamps) {
        if (id <= 0) throw new RuntimeException("Неверное значение для спикера");
        timestampService.saveTimestamp(id, timestamps);
    }


    @GetMapping(value = "/speaker")
    @GakSecured(roles = {Roles.PRESIDENT})
    public List<TimestampDto> getTimestampOfSpeaker(@RequestParam(name = "speakerId", required = true) Long id) {
        if (id <= 0) throw new RuntimeException("Неверное значение для спикера");
        return timestampService.getTimestampOfSpeaker(id).stream().map(TimestampDto::new).collect(Collectors.toList());
    }
}
