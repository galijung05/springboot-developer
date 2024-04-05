package me.blogSpringBoot.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 토큰 생성 요청 담당
public class CreateAccessTokenRequest {
    private String refreshToken;
}