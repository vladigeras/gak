package ru.iate.gak.service;

import ru.iate.gak.domain.Speaker;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface SpeakerService {
    void fillList(List<Speaker> speakers);
    List<Speaker> getSpeakerListOfCurrentGroupOfDay(String group, LocalDateTime date);
    List<File> getSpeakerProtocolsOfGroup(String group);
}
