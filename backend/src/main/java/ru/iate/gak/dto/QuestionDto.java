package ru.iate.gak.dto;

import ru.iate.gak.domain.Question;

public class QuestionDto extends LongIdentifiableDto {

    public String questionText;
    public DiplomDto diplom;

    public QuestionDto() {}

    public QuestionDto(Question question) {
        super(question.getId());
        this.questionText = question.getQuestionText();
        this.diplom = new DiplomDto(question.getDiplom());
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestionText(this.questionText);
        question.setDiplom(this.diplom.toDiplom());
        return question;
    }
}
