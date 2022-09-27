package com.example.demo.service.chat;

import com.example.demo.model.chat.ChatMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface MessageService {

    public void subscribe(Message<?> message, String clientIndex);
    public void unSubscribe(Message<?> message, String clientIndex, String[] chatIdAndToken);
    public void disconnect(Message<?> message, String clientIndex);
    public void message(ChatMessage chatMessage, Integer senderId);

}
