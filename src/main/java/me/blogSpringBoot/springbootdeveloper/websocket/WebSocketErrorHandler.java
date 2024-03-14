package me.blogSpringBoot.springbootdeveloper.websocket;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebSocketErrorHandler {

    // WebSocket 통신 중 컨트롤러나 다른 컴포넌트에서 예외가 발생 당 예외를 처리할 수 있는 @ExceptionHandler가 정의된 메서드 실행
    @ExceptionHandler(Exception.class)
    @SendTo("/topic/errors")
    public String handleException(Exception e) {
        // 실제 에러 핸들링 로직
        return e.getMessage();
    }
}
