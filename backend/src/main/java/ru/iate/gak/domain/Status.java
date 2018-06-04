package ru.iate.gak.domain;

public enum Status {
    ACTIVE,             // secretary doing that
    SPEAKING_TIME,      // when president start a speaking process
    SPEAKING_TIME_END,  // when president stop a speaking process
    REWIEW_TIME,
    REWIEW_TIME_END,
    QUESTION_TIME,      // ...............start a question process
    QUESTION_TIME_END,  // ...............stop a question process
    LASTWORD_TIME,
    LASTWORD_TIME_END,
    ALL_TIME,
    DONE                // end of the speaking
}
