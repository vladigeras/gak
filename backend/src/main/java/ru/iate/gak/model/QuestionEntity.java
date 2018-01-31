package ru.iate.gak.model;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class QuestionEntity extends LongIdentifiableEntity {

    @Column(name = "question")
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diplom_id", referencedColumnName = "id")
    private DiplomEntity diplom;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public DiplomEntity getDiplom() {
        return diplom;
    }

    public void setDiplom(DiplomEntity diplom) {
        this.diplom = diplom;
    }
}
