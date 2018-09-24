package ru.iate.gak.model;

public enum Status {
    ACTIVE,             // secretary doing that
    SPEAKING_TIME,      // when president start a speaking process
    REWIEW_TIME,
    QUESTION_TIME,      // ...............start a question process
    LASTWORD_TIME,
    DONE                // end of the speaking
}
