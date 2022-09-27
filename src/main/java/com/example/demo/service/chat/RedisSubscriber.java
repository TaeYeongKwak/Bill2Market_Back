package com.example.demo.service.chat;

import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageTemplate;

    public void sendMessage(String pubMessage){
        try {
            Map<String, Object> map = objectMapper.readValue(pubMessage, HashMap.class);
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatType(((String)map.get("chatType") == null)? ChatMessage.ChatType.MESSAGE:ChatMessage.ChatType.valueOf((String)map.get("chatType")))
                    .chatId((Integer)map.get("chatId"))
                    .senderId((Integer)map.get("senderId"))
                    .senderNickname((String)map.get("senderNickname"))
                    .message((String)map.get("message"))
                    .isRead((Boolean) map.get("isRead"))
                    .messageType(((String)map.get("messageType") == null)? null : MessageType.valueOf((String)map.get("messageType")))
                    .createDate((String)map.get("createDate"))
                    .build();
//            ChatMessage chatMessage = objectMapper.readValue(pubMessage, ChatMessage.class);
            messageTemplate.convertAndSend("/sub/chat/" + chatMessage.getChatId(), chatMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

}
