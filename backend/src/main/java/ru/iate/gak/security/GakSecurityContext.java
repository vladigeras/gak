package ru.iate.gak.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GakSecurityContext {

    private GakPrincipal principal;

    public GakPrincipal getCurrentPrincipal() {
        return principal;
    }

    public void setPrincipalIfAbsent(GakPrincipal principal) {
        if (this.principal != null) {
            return;
        }
        this.principal = principal;
    }
}
