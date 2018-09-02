package ru.iate.gak.dto;

import ru.iate.gak.model.Gender;
import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;

import java.util.Set;

public class UserDto extends LongIdentifiableDto {

    public String login;
    public String password;
    public String firstname;
    public String middlename;
    public String lastname;
    public Gender gender;
    public Set<Role> roles;

    public UserDto() {

    }

    public UserDto(UserEntity user) {
        super(user.getId());
        this.login = user.getLogin();
        this.password = null;
        this.firstname = user.getFirstname();
        this.middlename = user.getMiddlename();
        this.lastname = user.getLastname();
        this.gender = user.getGender();
        this.roles = user.getRoles();
    }
}
