package ru.iate.gak.service;

import ru.iate.gak.domain.Timestamp;

import java.util.List;

public interface TimestampService {

    void saveTimestamp(Long speakerId, List<Timestamp> timestamp);
    List<Timestamp> getTimestampOfSpeaker(Long speakerId);
}
