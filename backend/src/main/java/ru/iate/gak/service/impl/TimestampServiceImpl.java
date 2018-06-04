package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iate.gak.domain.Question;
import ru.iate.gak.domain.Timestamp;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.QuestionEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.model.TimestampEntity;
import ru.iate.gak.repository.QuestionRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.repository.TimestampRepository;
import ru.iate.gak.service.TimestampService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimestampServiceImpl implements TimestampService{

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private TimestampRepository timestampRepository;

    @Override
    @Transactional
    public void saveTimestamp(Long speakerId, List<Timestamp> timestamps) {
        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();

            if (diplomEntity != null) {
                timestampRepository.delete(diplomEntity.getTimestamps());

                timestamps.forEach(timestamp -> {
                    TimestampEntity timestampEntity = new TimestampEntity();
                    timestampEntity.setTimestamp(timestamp.getTimestamp());
                    timestampEntity.setStatus(timestamp.getStatus());
                    timestampEntity.setDiplom(diplomEntity);
                    timestampRepository.save(timestampEntity);
                });

            }
        } else throw new RuntimeException("Произошла ошибка");
    }

    @Override
    @Transactional
    public List<Timestamp> getTimestampOfSpeaker(Long speakerId) {

        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            if (speakerEntity.getStudent().getDiplom() != null) {
                DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();
                return timestampRepository.getAllByDiplom(diplomEntity).stream().map(Timestamp::new).collect(Collectors.toList());
            }
        } throw new RuntimeException("Произошла ошибка");
    }
}
