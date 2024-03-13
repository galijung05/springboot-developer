package me.blogSpringBoot.springbootdeveloper.websocket;

import me.blogSpringBoot.springbootdeveloper.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserChannelInterceptor implements ChannelInterceptor {

    private Map<String, String> subscribedUsers = new ConcurrentHashMap<>();
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private WebSocketController webSocketController;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        StompCommand command = sha.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            // 연결된 클라이언트의 정보를 저장
            String username = sha.getSessionId();
            sha.addNativeHeader("user-id", username);

            Map<String, List<String>> nativeHeaders = sha.toNativeHeaderMap();
            List<String> userIds = nativeHeaders.get("user-id");

            System.out.println("Headers: " + nativeHeaders);

            if (userIds != null && !userIds.isEmpty()) {
                System.out.println("Admin: " + userIds.get(0));
            }
            System.out.println("Username: " + username);

            for (Iterator<Map.Entry<String, String>> iterator = subscribedUsers.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                if (entry.getValue().equals(userIds.get(0))) {
                    iterator.remove(); // 이 value에 해당하는 key-value 쌍을 삭제한다.
                }
            }
            subscribedUsers.put(username, userIds.get(0));
        } else if (StompCommand.DISCONNECT.equals(command)) {
            // 연결이 종료된 클라이언트의 정보를 제거
            String sessionId = sha.getSessionId();

            String userId = subscribedUsers.get(sessionId); // sessionId(userId 키)로 userId를 맵에서 찾는다.

            if (userId != null) {
                subscribedUsers.remove(sessionId); // 해당 sessionId(userId 키)를 가진 엔트리를 제거한다.
                System.out.println("User removed: " + userId);
            }
            System.out.println("User disconnected: " + sessionId);
        }
        return message;
    }
    public Map<String,String> getSubscribedUsers() {
        return subscribedUsers;
    }

}