package ru.iate.gak.domain;

import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;

import java.util.Set;

public class User extends LongIdentifiable {

    private String login;
    private String password;
    private String firstname;
    private String middlename;
    private String lastname;
    private Set<Role> roles;

    public User() {

    }

    public User(UserEntity userEntity) {
        super(userEntity.getId());
        this.login = userEntity.getLogin();
        this.password = null;
        this.firstname = userEntity.getFirstname();
        this.middlename = userEntity.getMiddlename();
        this.lastname = userEntity.getMiddlename();
        this.roles = userEntity.getRoles();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
