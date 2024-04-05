package me.blogSpringBoot.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.blogSpringBoot.springbootdeveloper.config.jwt.TokenProvider;
import me.blogSpringBoot.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 유효한 토큰이면 리프레시 토큰으로 사용자ID 찾기
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        // 사용자ID로 사용자를 찾은 후에 토큰 제공자의 generateToken() 메서드를 호출해 새로운 액세스 토큰 생성
        User user = userService.findById(userId);
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
