package me.blogSpringBoot.springbootdeveloper.controller;

import lombok.Data;
import me.blogSpringBoot.springbootdeveloper.dto.RandomMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WebSocketController{

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Random random = new Random();

    // 클라이언트에서 보내는 메시지 내용
    @Data
    public static class MessageDTO {
        private String content;
    }

    // 클라이언트가 /app/message 로 메시지를 보낼 때 처리할 메서드
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public MessageDTO processMessageFromClient(MessageDTO message) {
        // 메시지 처리 로직
        System.out.println("Received message: " + message.getContent());
        return message; // 클라이언트로 메시지를 반환
    }

    // 클라이언트가 /app/random-message 로  오는 메시지를 받아 처리
    @MessageMapping("/random-message")
    // 구독하고 있는 클라이언트들에게 메시지를 발송
    @SendTo("/topic/randomMessage")
    public RandomMessageDTO sendRandomMessage() {
        // 랜덤 문자열과 숫자를 생성하는 로직
        String randomName = generateRandomString(10);
        String randomContent = generateRandomString(30);
        String randomUrl = "https://example.com/" + generateRandomString(5);

        RandomMessageDTO messageDTO = new RandomMessageDTO(
                                                                            UUID.randomUUID().toString(), // 랜덤 UUID
                                                                            random.nextInt(100), // 랜덤 숫자 (0~99)
                                                                            randomName, // 랜덤 이름
                                                                            randomUrl, // 랜덤 URL
                                                                            LocalDateTime.now(), // 현재 시간
                                                                            randomContent // 랜덤 채팅 내용
                                                                    );
        System.out.println(messageDTO);
        return messageDTO;
    }

    // 랜덤 문자열을 생성하는 도우미 메서드
    private String generateRandomString(int length) {
        return random.ints(97, 122) // ASCII 코드 'a'(97)에서 'z'(122)
                .limit(length)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
    }
}