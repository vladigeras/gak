package ru.iate.gak.service;

import ru.iate.gak.domain.Question;

import java.util.List;

public interface QuestionService {

    void saveQuestions(Long speakerId, List<Question> questions);
    List<Question> getQuestionsOfSpeaker(Long speakerId);
}
