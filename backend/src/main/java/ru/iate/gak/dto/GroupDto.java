package ru.iate.gak.dto;

import ru.iate.gak.domain.Group;

public class GroupDto {

    public String title;

    public GroupDto() {}

    public GroupDto(Group group) {
        this.title = group.getTitle();
    }

    public Group toGroup() {
        Group group = new Group();
        group.setTitle(this.title);
        return group;
    }
}
