package com.example.demo.service.chat;

import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.ChatMessageEvent;
import com.example.demo.repository.RedisChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService{

    private final RedisChatRepository redisChatRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void subscribe(Message<?> message, String clientIndex) {
        String chatId = getChatId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
        String lastUser = redisChatRepository.getLastUser(chatId);
        String sessionId = (String) message.getHeaders().get("simpSessionId");
        redisChatRepository.addUser(chatId, clientIndex, sessionId);

        if(!lastUser.equals(clientIndex))// 현재 접속자가 마지막 접속자가 아닌 경우
            redisChatRepository.resetNonReadCount(chatId);

        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.ENTER)
                .chatId(Integer.parseInt(chatId))
                .senderId(Integer.parseInt(clientIndex))
                .isRead(false)
                .build();

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    @Override
    public void unSubscribe(Message<?> message, String clientIndex, String[] chatIdAndToken) {
        String chatId = chatIdAndToken[0];
        redisChatRepository.deleteUser(chatId, clientIndex);

        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.EXIT)
                .chatId(Integer.parseInt(chatId))
                .senderId(Integer.parseInt(clientIndex))
                .build();

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);

    }

    @Override
    public void disconnect(Message<?> message, String clientIndex) {
        
    }

    @Override
    public void message(ChatMessage chatMessage, Integer senderId) {
        long userCount = redisChatRepository.getUserCount(String.valueOf(chatMessage.getChatId()));
        chatMessage.setSenderId(senderId);
        chatMessage.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if(userCount == 1){
            chatMessage.setIsRead(false);
            redisChatRepository.plusNonReadCount(String.valueOf(chatMessage.getChatId()));
        }else if(userCount == 2){
            chatMessage.setIsRead(true);
            redisChatRepository.resetNonReadCount(String.valueOf(chatMessage.getChatId()));
        }

        applicationEventPublisher.publishEvent(new ChatMessageEvent(applicationEventPublisher, chatMessage));
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }


    private String getChatId(String destination){
        int lastIndex = destination.lastIndexOf('/');
        return (lastIndex != -1)? destination.substring(lastIndex + 1) : "";
    }
}
