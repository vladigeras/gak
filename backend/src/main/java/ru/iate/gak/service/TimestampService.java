package ru.iate.gak.service;

import ru.iate.gak.dto.TimestampDto;
import ru.iate.gak.model.TimestampEntity;

import java.util.List;

public interface TimestampService {

    void saveTimestamp(Long speakerId, List<TimestampDto> timestamps);

    List<TimestampEntity> getTimestampOfSpeaker(Long speakerId);
}
