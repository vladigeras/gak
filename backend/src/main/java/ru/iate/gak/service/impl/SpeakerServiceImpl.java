package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import padeg.lib.Padeg;
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.model.*;
import ru.iate.gak.repository.*;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.service.TexService;

import javax.transaction.Transactional;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private TimestampRepository timestampRepository;

    /**
     * Clean existing speakers and fill new:
     * 1. Delete speaker.
     * 2. If new speaker has date, then save new speaker, else no save.
     */
    @Override
    @Transactional
    public void fillList(List<SpeakerDto> speakers) {
        List<SpeakerEntity> result = new ArrayList<>();
        speakers.forEach(s -> {
            if (s.student != null && s.student.id != null) {
                StudentEntity studentEntity = studentRepository.findOne(s.student.id);
                if (studentEntity == null)
                    throw new RuntimeException("Студент с id = " + s.student.id + " не найден");

                speakerRepository.deleteByStudent(studentEntity);

                if (s.date != null) {
                    SpeakerEntity speakerEntity = new SpeakerEntity();
                    speakerEntity.setListId(s.listId);
                    speakerEntity.setDate((s.date == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(s.date), ZoneOffset.UTC));
                    speakerEntity.setStudent(studentEntity);
                    speakerEntity.setOrderOfSpeaking(s.orderOfSpeaking);
                    result.add(speakerEntity);
                }
            } else throw new RuntimeException("Нет id у студента");
        });
        speakerRepository.save(result);
    }

    @Override
    @Transactional
    public List<SpeakerDto> getSpeakerListOfCurrentGroupOfDay(String group, LocalDateTime date) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        if (date == null) {
            return speakerRepository.getSpeakersListOfCurrentGroup(groupEntity).stream().map(SpeakerDto::new).collect(Collectors.toList());
        } else {
            return speakerRepository.getSpeakersListOfCurrentGroupOfDay(groupEntity, date).stream().map(SpeakerDto::new).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public Map<String, List<SpeakerDto>> getAllSpeakersListAllGroupsOfDay(LocalDateTime date) {
        Map<String, List<SpeakerDto>> result = new HashMap<>();
        groupRepository.getAllOrderByTitleAsc().forEach(groupEntity -> {   //get all groups and get speakers of these groups
            result.put(groupEntity.getTitle(), speakerRepository.getSpeakersListOfCurrentGroupOfDay(groupEntity, date).stream().map(SpeakerDto::new).collect(Collectors.toList()));
        });
        return result;
    }

    @Override
    @Transactional
    public List<File> getSpeakerProtocolsForTodaySpeakersOfGroup(String group) {
        GroupEntity groupEntity = groupRepository.findOne(group);
        if (groupEntity == null) throw new RuntimeException("Группа с названием " + group + "  не найдена");

        List<File> result = new ArrayList<>();

        LocalDateTime currentDayInUTC = LocalDateTime.now(ZoneOffset.UTC);  // this is a next day, because we need to get
        // objects of time included today, e.g. [-1day; now]

        List<SpeakerEntity> speakers = speakerRepository.getSpeakersListOfCurrentGroupBetweenDate(groupEntity, currentDayInUTC.minusDays(1), currentDayInUTC);
        speakers.forEach(s -> {
            if (s.getStudent() == null) return;

            DiplomEntity diplom = s.getStudent().getDiplom();

            if (diplom == null) return;
            if (diplom.getMentor() == null || diplom.getReviewer() == null)
                return;

            Map<String, String> params = new HashMap<>();

            params.put("@date", s.getDate().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); //because UTC date show another day
            params.put("@number", s.getOrderOfSpeaking().toString());

            boolean gender = false;     //false for FEMALE
            if (s.getStudent().getGender() != null && s.getStudent().getGender().equals(Gender.MALE)) gender = true;

            String studentName = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname(), "", gender, 2) :
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname(), s.getStudent().getMiddlename(), gender, 2);
            params.put("@studentRodit", studentName);
            params.put("@studentLastname", s.getStudent().getLastname());

            String studentIO = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    s.getStudent().getLastname() + " " + s.getStudent().getFirstname().charAt(0) + "." :
                    s.getStudent().getLastname() + " " + s.getStudent().getFirstname().charAt(0) + ". " + s.getStudent().getMiddlename().charAt(0) + ".";
            params.put("@studentIOIm", studentIO);

            String studentIORodit = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", "", gender, 2) :
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", s.getStudent().getMiddlename().charAt(0) + ".", gender, 2);
            params.put("@studentIORodit", studentIORodit);

            String studentIODatel = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", "", gender, 3) :
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", s.getStudent().getMiddlename().charAt(0) + ".", gender, 3);
            params.put("@studentIODatel", studentIODatel);

            String studentIOVinit = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", "", gender, 4) :
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", s.getStudent().getMiddlename().charAt(0) + ".", gender, 4);
            params.put("@studentIOVinit", studentIOVinit);

            String studentIOTvor = (s.getStudent().getMiddlename() == null) || (s.getStudent().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", "", gender, 5) :
                    Padeg.getFIOPadeg(s.getStudent().getLastname(), s.getStudent().getFirstname().charAt(0) + ".", s.getStudent().getMiddlename().charAt(0) + ".", gender, 5);
            params.put("@studentIOTvor", studentIOTvor);

            if (gender) {
                params.put("@genderStudentImen", "студент");
                params.put("@genderStudentRodit", "студента");
                params.put("@genderStudentDatel", "студенту");
                params.put("@genderStudentTvorit", "студентом");
                params.put("@genderDefImen", "защитил");
                params.put("@genderOutRodit", "выпускника");
                params.put("@genderOutDatel", "выпускнику");
            } else {
                params.put("@genderStudentImen", "студентка");
                params.put("@genderStudentRodit", "студентки");
                params.put("@genderStudentDatel", "студентке");
                params.put("@genderStudentTvorit", "студенткой");
                params.put("@genderDefImen", "защитила");
                params.put("@genderOutRodit", "выпускницу");
                params.put("@genderOutDatel", "выпускнице");
            }

            params.put("@title", diplom.getTitle());

            boolean mentorGender = false; //false for FEMALE
            if (diplom.getMentor().getGender() != null && diplom.getMentor().getGender().equals(Gender.MALE))
                mentorGender = true;

            String mentorName = (diplom.getMentor().getMiddlename() == null) || (diplom.getMentor().getMiddlename().isEmpty()) ?
                    diplom.getMentor().getLastname() + " " + diplom.getMentor().getFirstname().charAt(0) + "." :
                    diplom.getMentor().getLastname() + " " + diplom.getMentor().getFirstname().charAt(0) + ". " + diplom.getMentor().getMiddlename().charAt(0) + ".";
            params.put("@mentorIO", mentorName);

            String mentorRoditName = (diplom.getMentor().getMiddlename() == null) || (diplom.getMentor().getMiddlename().isEmpty()) ?
                    Padeg.getFIOPadeg(diplom.getMentor().getLastname(), diplom.getMentor().getFirstname(), "", mentorGender, 2) :
                    Padeg.getFIOPadeg(diplom.getMentor().getLastname(), diplom.getMentor().getFirstname(), diplom.getMentor().getMiddlename(), mentorGender, 2);
            params.put("@mentorRodit", mentorRoditName);

            String reviewerName = (diplom.getReviewer().getMiddlename() == null) || (diplom.getReviewer().getMiddlename().isEmpty()) ?
                    diplom.getReviewer().getLastname() + " " + diplom.getReviewer().getFirstname().charAt(0) + "." :
                    diplom.getReviewer().getLastname() + " " + diplom.getReviewer().getFirstname().charAt(0) + ". " + diplom.getReviewer().getMiddlename().charAt(0) + ".";
            params.put("@reviewerIO", reviewerName);

            Set<QuestionEntity> questions = diplom.getQuestions();
            final int[] i = {1};      //only 6 first question write in protocol
            questions.forEach(q -> {
                if (i[0] > 6) return;
                params.put("@question" + i[0], q.getQuestionText());
                i[0]++;
            });

            if (questions.size() < 6) {     //if true, then last question keys must be empty
                for (int k = 6; k > questions.size(); k--) params.put("@question" + k, null);
            }

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

            List<TimestampEntity> timestamps = timestampRepository.getAllByDiplom(diplom);
            final LocalDateTime[] speakingTimeStart = {null};
            final LocalDateTime[] speakingTimeEnd = {null};

            timestamps.forEach(t -> {
                if (t.getStatus().equals(Status.SPEAKING_TIME)) speakingTimeStart[0] = t.getTimestamp();
                if (t.getStatus().equals(Status.SPEAKING_TIME_END)) speakingTimeEnd[0] = t.getTimestamp();
            });

            if (speakingTimeStart[0] != null && speakingTimeEnd[0] != null) {
                Long speakingTime = speakingTimeStart[0].until(speakingTimeEnd[0], ChronoUnit.MINUTES);
                params.put("@time", speakingTime.toString());
            } else params.put("@time", null);

            Integer resultMark = diplom.getResultMark();    //main result of diplom
            if (resultMark != null) {
                String ects = null;

                if (resultMark >= 90) ects = "A";
                if (resultMark >= 85 && resultMark <= 89) ects = "B";
                if (resultMark >= 75 && resultMark <= 84) ects = "C";
                if (resultMark >= 65 && resultMark <= 74) ects = "D";
                if (resultMark >= 60 && resultMark <= 64) ects = "E";
                if (resultMark < 60) ects = "F";

                params.put("@result", resultMark.toString());
                params.put("@ects", ects);
            } else {
                params.put("@result", null);
                params.put("@ects", null);
            }

            result.add(texService.exportDocuments(params));
        });

        return result;
    }

    @Override
    @Transactional
    public SpeakerEntity updateDiplomStatus(Long speakerId, Status status) {
        if (speakerId > 0) {
            SpeakerEntity speakerEntity = speakerRepository.getOne(speakerId);
            if (speakerEntity != null) {
                if (speakerEntity.getStudent() != null) {
                    if (speakerEntity.getStudent().getDiplom() != null) {
                        speakerEntity.getStudent().getDiplom().setStatus(status);
                        speakerRepository.save(speakerEntity);

                        return speakerEntity;
                    } else throw new RuntimeException("Произошла ошибка");
                } else throw new RuntimeException("Произошла ошибка");
            } else throw new RuntimeException("Произошла ошибка");
        } else throw new RuntimeException("Произошла ошибка");
    }

}
