package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iate.gak.dto.TimestampDto;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.model.TimestampEntity;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.repository.TimestampRepository;
import ru.iate.gak.service.TimestampService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TimestampServiceImpl implements TimestampService {

	@Autowired
	private SpeakerRepository speakerRepository;

	@Autowired
	private TimestampRepository timestampRepository;

	@Override
	@Transactional
	public void saveTimestamp(Long speakerId, List<TimestampDto> timestamps) {
		SpeakerEntity speakerEntity = speakerRepository.findById(speakerId).orElseThrow(() -> new RuntimeException("Спикер с id = " + speakerId + " не найден"));

		if (speakerEntity.getStudent() != null) {
			DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();

			if (diplomEntity != null) {
				timestampRepository.deleteAll(diplomEntity.getTimestamps());

				timestamps.forEach(timestamp -> {
					TimestampEntity timestampEntity = new TimestampEntity();
					timestampEntity.setTimestamp((timestamp.timestamp == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.timestamp), ZoneOffset.UTC));
					timestampEntity.setStatus(timestamp.status);
					timestampEntity.setDiplom(diplomEntity);
					timestampRepository.save(timestampEntity);
				});

			}

		} else throw new RuntimeException("Произошла ошибка");
	}

	@Override
	@Transactional
	public List<TimestampEntity> getTimestampOfSpeaker(Long speakerId) {
		SpeakerEntity speakerEntity = speakerRepository.findById(speakerId).orElseThrow(() -> new RuntimeException("Спикер с id = " + speakerId + " не найден"));

		if (speakerEntity.getStudent() != null) {
			if (speakerEntity.getStudent().getDiplom() != null) {
				DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();
				return timestampRepository.getAllByDiplom(diplomEntity);
			}
		}
		throw new RuntimeException("Произошла ошибка");
	}
}
