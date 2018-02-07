package ru.iate.gak.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private GakSecurityContext securityContext;

    @GetMapping(value = "/principal")
    public PrincipalDto principal() {
        return new PrincipalDto(securityContext.getCurrentPrincipal());
    }

    @PostMapping(value = "/login")
    public AuthTokenDto login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Authentication auth = securityService.auth(loginDto.login, loginDto.password);

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        UserDetails userDetails = (UserDetails)auth.getDetails();
        String authToken = authTokenService.generateTokenFromString(userDetails.getUsername());

        //fill cookie
        Cookie cookie = new Cookie(SecurityConstants.X_AUTH_TOKEN, authToken);
        cookie.setPath("/");
        cookie.setMaxAge(SecurityConstants.EXPIRATION_TIME);
        response.addCookie(cookie);

        //fill result
        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.token = authToken;
        return authTokenDto;
    }

    @GetMapping(value = "/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(SecurityConstants.X_AUTH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
