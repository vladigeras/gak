package ru.iate.gak.security;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public class GakPrincipal implements Principal {
    private Long id;
    private String name;
    private Set<String> roles;

    public GakPrincipal(Long id, String name, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    /**
     * Returns true if the specified subject is implied by this principal.
     * <p>
     * <p>The default implementation of this method returns true if
     * {@code subject} is non-null and contains at least one principal that
     * is equal to this principal.
     * <p>
     * <p>Subclasses may override this with a different implementation, if
     * necessary.
     *
     * @param subject the {@code Subject}
     * @return true if {@code subject} is non-null and is
     * implied by this principal, or false otherwise.
     * @since 1.8
     */
    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return new HashSet<>(roles);
    }

}
