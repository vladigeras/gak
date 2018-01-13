package ru.iate.gak.security;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.SignatureException;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthTokenFilter implements Filter {

    @Autowired
    private GakSecurityContext securityContext;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        String token = extractTokenFromRequest(request);

        if (token != null && !token.trim().isEmpty()) {
            try {
                //Попытка создания объекта аутентификации
                String login = authTokenService.parseStringFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(login);
                Authentication authentication = new CustomAuthentication(userDetails);
                authentication.setAuthenticated(true);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                //Кладем объект пользователя в собственный контекст безопасности
                fillSecurityContext(userDetails);
            } catch (Exception e) {
                if (e instanceof UsernameNotFoundException
                        || e instanceof SignatureException
                        || e instanceof MalformedJwtException) {
                    //Сбрасываем куки
                    resetAuthToken(servletResponse);
                } else {
                    throw e;
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        //Ищем в куках токен аутентификации
        String token = extractTokenFromCookies(request.getCookies());

        //Если не нашли токен в куках, то ищем в заголовках
        if (token == null) {
            token = request.getHeader(SecurityConstants.X_AUTH_TOKEN);
        }
        return token;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SecurityConstants.X_AUTH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void fillSecurityContext(UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByLogin(userDetails.getUsername());

        Set<String> roles = new HashSet<>();
        userEntity.getRoles().forEach(r -> roles.add(r.name()));
        GakPrincipal principal = new GakPrincipal(userEntity.getId(), userEntity.getLogin(), roles);

        securityContext.setPrincipalIfAbsent(principal);
    }

    private void resetAuthToken(ServletResponse servletResponse) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Cookie cookie = new Cookie(SecurityConstants.X_AUTH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
