package me.blogSpringBoot.springbootdeveloper.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFaiureHandler implements AuthenticationFailureHandler {
    private final AuthenticationService service;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,  AuthenticationException exception) throws IOException, ServletException {
        service.failedLogin(request, response);
    }
}
