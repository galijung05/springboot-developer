package me.blogSpringBoot.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    // H2 콘솔과 정적 리소스(/static/**)에 대한 접근을 허용
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .requestMatchers("/login", "/signup", "/user", "/ws/**").permitAll() // 해당 경로에 대한 요청은 인증 없이 허용
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/articles") // 로그인 페이지를 /login으로 설정하고, 성공 시 /articles로 리디렉션
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true) // 로그아웃 시 /login으로 리디렉션하고 세션을 무효화
                .and()
                .csrf().disable() // CSRF 보호를 비활성화
                .build();
    }

    // BCryptPasswordEncoder를 사용하여 비밀번호를 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}