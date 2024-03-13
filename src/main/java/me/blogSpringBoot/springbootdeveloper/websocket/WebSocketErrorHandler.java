package me.blogSpringBoot.springbootdeveloper.websocket;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebSocketErrorHandler {

    @ExceptionHandler(Exception.class)
    @SendTo("/topic/errors")
    public String handleException(Exception e) {
        // 실제 에러 핸들링 로직
        return e.getMessage();
    }
}
