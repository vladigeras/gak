package ru.iate.gak.service;

import ru.iate.gak.dto.QuestionDto;
import ru.iate.gak.model.QuestionEntity;

import java.util.List;

public interface QuestionService {

    void saveQuestions(Long speakerId, List<QuestionDto> questions);

    List<QuestionEntity> getQuestionsOfSpeaker(Long speakerId);
}
