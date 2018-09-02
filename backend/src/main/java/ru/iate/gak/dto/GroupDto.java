package ru.iate.gak.dto;

import ru.iate.gak.model.GroupEntity;

public class GroupDto {

    public String title;

    public GroupDto() {}

    public GroupDto(GroupEntity group) {
        this.title = group.getTitle();
    }
}
