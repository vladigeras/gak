package ru.iate.gak.dto;

import ru.iate.gak.domain.User;
import ru.iate.gak.domain.Role;

import java.util.Set;

public class UserDto extends LongIdentifiableDto {

    public String login;
    public String password;
    public String firstname;
    public String middlename;
    public String lastname;
    public Set<Role> roles;

    public UserDto() {

    }

    public UserDto(User user) {
        super(user.getId());
        this.login = user.getLogin();
        this.password = null;
        this.firstname = user.getFirstname();
        this.middlename = user.getMiddlename();
        this.lastname = user.getLastname();
        this.roles = user.getRoles();
    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setLogin(this.login);
        user.setPassword(this.password);
        user.setFirstname(this.firstname);
        user.setMiddlename(this.middlename);
        user.setLastname(this.lastname);
        user.setRoles(this.roles);
        return user;
    }
}
