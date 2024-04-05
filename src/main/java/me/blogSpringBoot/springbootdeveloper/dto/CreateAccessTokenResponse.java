package me.blogSpringBoot.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
// 토큰 응답 담당
public class CreateAccessTokenResponse {
    private String accessToken;
}