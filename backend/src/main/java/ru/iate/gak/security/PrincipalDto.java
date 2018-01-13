package ru.iate.gak.security;

import java.util.Set;

public class PrincipalDto {

    public Object id;
    public String name;
    public Set<String> roles;

    public PrincipalDto(GakPrincipal principal) {

        if (principal != null) {
            id = principal.getId();
            name = principal.getName();
            roles = principal.getRoles();
        }
    }
}
