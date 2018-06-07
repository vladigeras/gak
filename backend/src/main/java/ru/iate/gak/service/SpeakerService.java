package ru.iate.gak.service;

import ru.iate.gak.domain.Speaker;
import ru.iate.gak.domain.Status;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SpeakerService {
    void fillList(List<Speaker> speakers);
    List<Speaker> getSpeakerListOfCurrentGroupOfDay(String group, LocalDateTime date);
    Map<String, List<Speaker>> getAllSpeakersListAllGroupsOfDay(LocalDateTime date);
    List<File> getSpeakerProtocolsForTodaySpeakersOfGroup(String group);
    Speaker updateDiplomStatus(Long speakerId, Status status);
}
