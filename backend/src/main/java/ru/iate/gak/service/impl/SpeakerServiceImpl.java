package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Role;
import ru.iate.gak.domain.Speaker;
import ru.iate.gak.domain.Status;
import ru.iate.gak.model.*;
import ru.iate.gak.repository.CommissionRepository;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.repository.StudentRepository;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.service.TexService;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeakerServiceImpl implements SpeakerService {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TexService texService;

    @Autowired
    private CommissionRepository commissionRepository;

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

    @Override
    @Transactional
    public List<File> getSpeakerProtocolsOfGroup(String group) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        List<File> result = new ArrayList<>();
        List<SpeakerEntity> speakers = speakerRepository.getSpeakersListOfCurrentGroup(groupEntity);
        speakers.forEach(s -> {
            if (s.getStudent() == null) return;
            if (s.getStudent().getDiplom() == null) return;
            if (s.getStudent().getDiplom().getMentor() == null || s.getStudent().getDiplom().getReviewer() == null)
                return;

            Map<String, String> params = new HashMap<>();
            params.put("@date", s.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            params.put("@number", s.getOrderOfSpeaking().toString());

            String studentName = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    s.getStudent().getLastname() + " " + s.getStudent().getFirstname() :
                    s.getStudent().getLastname() + " " + s.getStudent().getFirstname() + " " + s.getStudent().getMiddlename();
            params.put("@student", studentName);
            params.put("@studentLastname", s.getStudent().getLastname());

            params.put("@title", s.getStudent().getDiplom().getTitle());

            String mentorName = (s.getStudent().getDiplom().getMentor().getMiddlename() == null) || (s.getStudent().getDiplom().getMentor().getMiddlename().isEmpty()) ?
                    s.getStudent().getDiplom().getMentor().getLastname() + " " + s.getStudent().getDiplom().getMentor().getFirstname().charAt(0) + "." :
                    s.getStudent().getDiplom().getMentor().getLastname() + " " + s.getStudent().getDiplom().getMentor().getFirstname().charAt(0) + ". " + s.getStudent().getDiplom().getMentor().getMiddlename().charAt(0) + ".";
            params.put("@mentor", mentorName);

            String reviewerName = (s.getStudent().getDiplom().getReviewer().getMiddlename() == null) || (s.getStudent().getDiplom().getReviewer().getMiddlename().isEmpty()) ?
                    s.getStudent().getDiplom().getReviewer().getLastname() + " " + s.getStudent().getDiplom().getReviewer().getFirstname().charAt(0) + "." :
                    s.getStudent().getDiplom().getReviewer().getLastname() + " " + s.getStudent().getDiplom().getReviewer().getFirstname().charAt(0) + ". " + s.getStudent().getDiplom().getReviewer().getMiddlename().charAt(0) + ".";
            params.put("@reviewer", reviewerName);

            Set<QuestionEntity> questions = s.getStudent().getDiplom().getQuestions();
            final int[] i = {1};      //only 6 first question write in protocol
            questions.forEach(q -> {
                if (i[0] > 6) return;
                params.put("@question" + i[0], q.getQuestionText());
                i[0]++;
            });

            List<CommissionEntity> commissions = commissionRepository.getCommissionEntitiesByListId(1);
            final int[] k = {1}; //only 4 gak member + president + secretary must be
            commissions.forEach(c -> {
                if (c.getUser() == null) return;

                String userName = (c.getUser().getMiddlename() == null) || (c.getUser().getMiddlename().isEmpty()) ?
                        c.getUser().getLastname() + " " + c.getUser().getFirstname().charAt(0) + "." :
                        c.getUser().getLastname() + " " + c.getUser().getFirstname().charAt(0) + ". " + c.getUser().getMiddlename().charAt(0) + ".";
                if (c.getUser().getRoles().contains(Role.PRESIDENT)) {
                    params.put("@president", userName);
                    return;
                }
                if (c.getUser().getRoles().contains(Role.SECRETARY)) {
                    params.put("@secretary", userName);
                    return;
                }
                if (c.getUser().getRoles().contains(Role.MEMBER)) {
                    if (k[0] > 4) return;
                    params.put("@member" + k[0], userName);
                    k[0]++;
                    return;
                }
            });

            params.put("@mark5", null);
            params.put("@mark4", null);
            params.put("@mark3", null);
            params.put("@markAverage", null);
            params.put("@time", null);

            result.add(texService.exportDocuments(params));
        });

        return result;
    }

    @Override
    @Transactional
    public Speaker updateDiplomStatus(Long speakerId, Status status) {
        if (speakerId > 0) {
            SpeakerEntity speakerEntity = speakerRepository.getOne(speakerId);
            if (speakerEntity != null) {
                if (speakerEntity.getStudent() != null) {
                    if (speakerEntity.getStudent().getDiplom() != null) {
                        speakerEntity.getStudent().getDiplom().setStatus(status);
                        speakerRepository.save(speakerEntity);

                        return new Speaker(speakerEntity);
                    } else throw new RuntimeException("Произошла ошибка");
                } else throw new RuntimeException("Произошла ошибка");
            } else throw new RuntimeException("Произошла ошибка");
        } else throw new RuntimeException("Произошла ошибка");
    }
}
