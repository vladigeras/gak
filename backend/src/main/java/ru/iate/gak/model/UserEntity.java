package ru.iate.gak.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends LongIdentifiableEntity {

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "lastname")
    private String lastname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"})
    )
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentor")
    private Set<DiplomEntity> diplomsWhereUserIsMentor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewer")
    private Set<DiplomEntity> diplomsWhereUserIsReviewer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<CommissionEntity> commissionsWhereUserInclude;

    public UserEntity() {
    }

    public UserEntity(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
