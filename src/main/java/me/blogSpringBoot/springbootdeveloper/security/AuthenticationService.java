package me.blogSpringBoot.springbootdeveloper.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blogSpringBoot.springbootdeveloper.common.model.AdminResponse;
import me.blogSpringBoot.springbootdeveloper.domain.AdminProperties;
import me.blogSpringBoot.springbootdeveloper.domain.AdminUser;
import me.blogSpringBoot.springbootdeveloper.service.AdminUserService;
import me.blogSpringBoot.springbootdeveloper.service.PropertiesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AdminUserService adminUserService;
    private final PropertiesService propertiesService;

    // 사용자 이름으로 AdminUser를 찾고, 로그인 처리를 수행
    public void successfulLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String userName = request.getParameter("j_username");
        Optional<AdminUser> loginedUserIfNull = adminUserService.findByUsername(userName);
        if(!loginedUserIfNull.isPresent()) {
            loginResponse(401, null, response);
            return;
        }

        AdminUser loginedUser = loginedUserIfNull.get();

        AdminProperties props = AdminProperties.builder().build();
        try {
            props = propertiesService.get();
        } catch (Exception e) {}

        final int sessionExpiredTime =
                props.getCheckSessionExpired() ==
                        AdminProperties.CheckType.UNCHECK ? props.getSessionExpiredTime() : -1; // 180분
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(sessionExpiredTime);
        loginedUser.setSessionIntervalTime(sessionExpiredTime);
        loginedUser.setCheckSessionExpired(props.getCheckSessionExpired());

        try {
            adminUserService.update(loginedUser.getAdminUserId(), loginedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginResponse(200, loginedUser, response);
    }

    // 실패한 로그인 시도를 처리
    public void failedLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("j_username");
        Optional<AdminUser> loginedUserIfNull = adminUserService.findByUsername(userName);
        if(!loginedUserIfNull.isPresent()) {
            loginResponse(401, null, response);
            return;
        }

        AdminProperties props = AdminProperties.builder().build();
        try {
            props = propertiesService.get();
        } catch (Exception e) {}

        AdminUser admin = loginedUserIfNull.get();
        try {
            adminUserService.update(admin.getAdminUserId(), admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginResponse(401, null, response);

    }

    // HTTP 응답을 구성하고, JSON 형태로 변환하여 반환
    private void loginResponse(int status, AdminUser user, HttpServletResponse response) throws IOException  {
        response.setContentType("application/json;charset=UTF8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(AdminResponse
                .builder()
                .status(status)
                .payload(user)
                .build());
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }


    //로그아웃 처리를 수행
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        loginResponse(200, null, response);
    }
}
