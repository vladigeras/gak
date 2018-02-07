package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.util.StringUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/speakers")
public class SpeakerController {

    @Autowired
    private SpeakerService speakerService;

    @PostMapping(value = "/save", consumes = "application/json")
    @GakSecured(roles = {Roles.ADMIN})
    public void saveList(@RequestBody List<SpeakerDto> speakers) {
        speakerService.fillList(speakers.stream().map(SpeakerDto::toSpeaker).collect(Collectors.toList()));
    }

    @GetMapping(value = "/ofGroupOfDay")
    public List<SpeakerDto> getSpeakerListOfGroupToday(@RequestParam(value = "group") String group,
                                                       @RequestParam(value = "date", required = false) Long date) {
        if (StringUtil.isStringNullOrEmptyTrim(group)) throw new RuntimeException("Неверное значение для группы");
        LocalDateTime localDateTime = (date == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);

        return speakerService.getSpeakerListOfCurrentGroupOfDay(group, localDateTime).stream().map(SpeakerDto::new).collect(Collectors.toList());
    }
}
