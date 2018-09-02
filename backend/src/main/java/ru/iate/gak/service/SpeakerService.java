package ru.iate.gak.service;

import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.model.Status;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SpeakerService {
    void fillList(List<SpeakerDto> speakers);

    List<SpeakerDto> getSpeakerListOfCurrentGroupOfDay(String group, LocalDateTime date);

    Map<String, List<SpeakerDto>> getAllSpeakersListAllGroupsOfDay(LocalDateTime date);
    List<File> getSpeakerProtocolsForTodaySpeakersOfGroup(String group);

    SpeakerEntity updateDiplomStatus(Long speakerId, Status status);
}
