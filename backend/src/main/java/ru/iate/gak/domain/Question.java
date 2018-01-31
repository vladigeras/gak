package ru.iate.gak.domain;

import ru.iate.gak.model.QuestionEntity;

public class Question extends LongIdentifiable {

    private String questionText;
    private Diplom diplom;

    public Question() {}

    public Question(QuestionEntity questionEntity) {
        super(questionEntity.getId());
        this.questionText = questionEntity.getQuestionText();
        this.diplom = new Diplom(questionEntity.getDiplom());
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Diplom getDiplom() {
        return diplom;
    }

    public void setDiplom(Diplom diplom) {
        this.diplom = diplom;
    }
}
