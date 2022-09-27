package com.example.demo.config.websocket;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        accessor.setLeaveMutable(false);
        String token = accessor.getFirstNativeHeader("Authorization");
        if(StompCommand.CONNECT == accessor.getCommand())
            jwtTokenProvider.validateToken(token);
        else if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            messageService.subscribe(message, auth.getName());
        }
        else if(StompCommand.DISCONNECT == accessor.getCommand()){

        }
        else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()){
            String[] chatIdAndToken = accessor.getFirstNativeHeader("id").split("/");
            Authentication auth = jwtTokenProvider.getAuthentication(chatIdAndToken[1]);
            messageService.unSubscribe(message, auth.getName(), chatIdAndToken);
        }
        return message;
    }

}
