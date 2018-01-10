package ru.iate.gak.model;

import ru.iate.gak.domain.Role;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "title")
    private Role role;

    public RoleEntity() {
    }

    public RoleEntity(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
