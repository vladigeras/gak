package ru.iate.gak.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iate.gak.dto.QuestionDto;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.QuestionEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.repository.QuestionRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.service.QuestionService;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    @Transactional
    public void saveQuestions(Long speakerId, List<QuestionDto> questions) {
        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();

            if (diplomEntity != null) {
                questionRepository.delete(diplomEntity.getQuestions());

                questions.forEach(question -> {
                    QuestionEntity questionEntity = new QuestionEntity();
                    questionEntity.setQuestionText(question.questionText);
                    questionEntity.setDiplom(diplomEntity);
                    questionRepository.save(questionEntity);
                });

                logger.info("К диплому " + diplomEntity.getId() + " сохранены вопросы");
            }
        } else throw new RuntimeException("Произошла ошибка");
    }

    @Override
    @Transactional
    public List<QuestionEntity> getQuestionsOfSpeaker(Long speakerId) {

        SpeakerEntity speakerEntity = speakerRepository.findOne(speakerId);
        if (speakerEntity == null) throw new RuntimeException("Спикер с id = " + speakerId + " не найден");

        if (speakerEntity.getStudent() != null) {
            if (speakerEntity.getStudent().getDiplom() != null) {
                DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();
                return questionRepository.getAllByDiplom(diplomEntity);
            }
        }
        throw new RuntimeException("Произошла ошибка");
    }
}
