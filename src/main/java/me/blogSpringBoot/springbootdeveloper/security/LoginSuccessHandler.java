package me.blogSpringBoot.springbootdeveloper.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final AuthenticationService service;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        System.out.println("Login ");
        service.successfulLogin(request, response, authentication);

        String currentSessionId = request.getSession().getId().toString();
        String username = authentication.getName();
        String principalIndexKey = "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:" + username;
        System.out.println("currentSessionId : " + currentSessionId);

        // 세션 키 목록 가져오기
        Set<String> sessionKeys = redisTemplate.opsForSet().members(principalIndexKey);

        List<String> modifiedSessionKeys = new ArrayList<>();

        Set<String> validSessionKeys = new HashSet<>(); // 만료세션 지우고 담는 배열
        Pattern pattern = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");

        for (String sessionKey : sessionKeys) {
            Matcher matcher = pattern.matcher(sessionKey);
            if (matcher.find()) {
                String modifiedSessionKey = matcher.group();
                modifiedSessionKeys.add(modifiedSessionKey);
                if (redisTemplate.hasKey("spring:session:sessions:" + modifiedSessionKey)) {
                    validSessionKeys.add(modifiedSessionKey);
                }
            }
        }

        if(validSessionKeys.size()>1){
            for (String sessionKey : validSessionKeys) {
                if (!sessionKey.equals(currentSessionId)) {

                    // Delete session
                    String sessionDataKey = "spring:session:sessions:" + sessionKey;
                    String sessionExpireKey = "spring:session:sessions:expires:" + sessionKey;

                    redisTemplate.execute(new SessionCallback<Object>() {
                        @Override
                        public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                            operations.multi();
                            redisTemplate.delete(sessionDataKey);
                            redisTemplate.delete(sessionExpireKey);
                            return operations.exec();
                        }}

                    );

                }
            }
        }
    }
}