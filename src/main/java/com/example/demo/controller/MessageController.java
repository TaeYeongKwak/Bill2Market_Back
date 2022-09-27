package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.model.chat.ChatMessage;
import com.example.demo.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage, @Header("Authorization") String token){
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        messageService.message(chatMessage, Integer.parseInt(auth.getName()));
    }

}
