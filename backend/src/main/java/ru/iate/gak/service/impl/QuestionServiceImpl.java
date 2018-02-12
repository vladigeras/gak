package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iate.gak.domain.Question;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.QuestionEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.repository.QuestionRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    @Transactional
    public void saveQuestions(Long speakerId, List<Question> questions) {
        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();

            if (diplomEntity != null) {
                questionRepository.delete(diplomEntity.getQuestions());

                questions.forEach(question -> {
                    QuestionEntity questionEntity = new QuestionEntity();
                    questionEntity.setQuestionText(question.getQuestionText());
                    questionEntity.setDiplom(diplomEntity);
                    questionRepository.save(questionEntity);
                });

            }
        } else throw new RuntimeException("Произошла ошибка");
    }

    @Override
    @Transactional
    public List<Question> getQuestionsOfSpeaker(Long speakerId) {

        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            if (speakerEntity.getStudent().getDiplom() != null) {
                DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();
                return questionRepository.getAllByDiplom(diplomEntity).stream().map(Question::new).collect(Collectors.toList());
            }
        } throw new RuntimeException("Произошла ошибка");
    }
}
