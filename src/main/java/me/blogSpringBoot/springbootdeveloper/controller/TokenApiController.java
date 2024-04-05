package me.blogSpringBoot.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.blogSpringBoot.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.blogSpringBoot.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.blogSpringBoot.springbootdeveloper.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 토큰 요청 및 처리 담당 컨트롤러
@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    // 리프레시 토큰을 기반으로 새로운 액세스 코드 생성
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken (@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

}
