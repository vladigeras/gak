package ru.iate.gak.domain;

public enum Status {
    ACTIVE,             // secretary doing that
    SPEAKING_TIME,      // when president start a speaking process
    SPEAKING_TIME_END,  // when president stop a speaking process
    QUESTION_TIME,      // ...............start a question process
    QUESTION_TIME_END,  // ...............stop a question process
    DONE                // end of the speaking
}
