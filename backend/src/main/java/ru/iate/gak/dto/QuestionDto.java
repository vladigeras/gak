package ru.iate.gak.dto;

import ru.iate.gak.model.QuestionEntity;

public class QuestionDto extends LongIdentifiableDto {

    public String questionText;
    public DiplomDto diplom;

    public QuestionDto() {}

    public QuestionDto(QuestionEntity question) {
        super(question.getId());
        this.questionText = question.getQuestionText();
        this.diplom = new DiplomDto(question.getDiplom());
    }
}
