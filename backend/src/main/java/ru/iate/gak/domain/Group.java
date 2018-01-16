package ru.iate.gak.domain;

import ru.iate.gak.model.GroupEntity;

public class Group {
    private String title;

    public Group() {}

    public Group(GroupEntity groupEntity) {
        this.title = groupEntity.getTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
