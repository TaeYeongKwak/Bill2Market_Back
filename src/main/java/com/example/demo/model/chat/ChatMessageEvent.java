package com.example.demo.model.chat;

import org.springframework.context.ApplicationEvent;

public class ChatMessageEvent extends ApplicationEvent {

    private ChatMessage chatMessage;

    public ChatMessageEvent(Object source, ChatMessage chatMessage) {
        super(source);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage(){
        return chatMessage;
    }

}
