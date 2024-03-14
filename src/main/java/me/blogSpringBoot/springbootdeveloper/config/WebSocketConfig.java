package me.blogSpringBoot.springbootdeveloper.config;

import me.blogSpringBoot.springbootdeveloper.websocket.UserChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserChannelInterceptor userChannelInterceptor;

    @Value("${react.server.front_url}")
    private String frontDomain;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket 서버에 연결할 때 사용할 엔드포인트를 등록
        registry.addEndpoint("/ws").setAllowedOrigins(frontDomain).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 애플리케이션이 처리하는 메시지의 대상(prefix)을 설정
        // "/app"로 시작하는 목적지를 가진 메시지는 @MessageMapping 어노테이션이 붙은 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/app");
        // "/topic"로 시작하는 목적지를 가진 메시지는 Simple 메시지 브로커로 라우팅해 메시지를 구독하는 클라이언트에게 메시지 전송
        registry.enableSimpleBroker("/topic");
    }

}