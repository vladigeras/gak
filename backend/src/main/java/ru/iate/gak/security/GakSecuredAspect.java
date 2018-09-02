package ru.iate.gak.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.iate.gak.model.Role;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class GakSecuredAspect {

    private static final String ACCESS_DENIED_ERROR = "Доступ запрещен";

    @Autowired
    private GakSecurityContext gakSecurityContext;

    @Before("@annotation(ru.iate.gak.security.GakSecured)")
    public void checkAccess(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        GakSecured secured = method.getAnnotation(GakSecured.class);

        Set<String> roles = Arrays.stream(secured.roles())
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            if (!secured.role().equals("")) {
                roles.add(secured.role());
            }
        }
        GakPrincipal principal = gakSecurityContext.getCurrentPrincipal();

        if (principal == null) {
            throw new RuntimeException(ACCESS_DENIED_ERROR);
        }

        if (principal.getRoles().contains(Role.ADMIN)) {
            return;
        }

        if (!roles.isEmpty() && !CollectionUtils.containsAny(roles, principal.getRoles())) {
            throw new RuntimeException(ACCESS_DENIED_ERROR);
        }
    }
}
