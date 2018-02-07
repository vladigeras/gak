package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Speaker;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.model.StudentEntity;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.repository.StudentRepository;
import ru.iate.gak.service.SpeakerService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpeakerServiceImpl implements SpeakerService {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;


    /**
     * Clean existing speakers and fill new:
     * 1. Delete speaker.
     * 2. If new speaker has date, then save new speaker, else no save.
     */
    @Override
    @Transactional
    public void fillList(List<Speaker> speakers) {
        List<SpeakerEntity> result = new ArrayList<>();
        speakers.forEach(s -> {
            if (s.getStudent() != null && s.getStudent().getId() != null) {
                StudentEntity studentEntity = studentRepository.findOne(s.getStudent().getId());
                if (studentEntity == null)
                    throw new RuntimeException("Студент с id = " + s.getStudent().getId() + " не найден");

                speakerRepository.deleteByStudent(studentEntity);

                if (s.getDate() != null) {
                    SpeakerEntity speakerEntity = new SpeakerEntity();
                    speakerEntity.setListId(s.getListId());
                    speakerEntity.setDate(s.getDate());
                    speakerEntity.setStudent(studentEntity);
                    speakerEntity.setOrderOfSpeaking(s.getOrderOfSpeaking());
                    result.add(speakerEntity);
                }
            } else throw new RuntimeException("Нет id у студента");
        });
        speakerRepository.save(result);
    }

    @Override
    @Transactional
    public List<Speaker> getSpeakerListOfCurrentGroupOfDay(String group, LocalDateTime date) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        if (date == null) {
            return speakerRepository.getSpeakersListOfCurrentGroup(groupEntity).stream().map(Speaker::new).collect(Collectors.toList());
        } else {
            return speakerRepository.getSpeakersListOfCurrentGroupOfDay(groupEntity, date).stream().map(Speaker::new).collect(Collectors.toList());
        }
    }

}
