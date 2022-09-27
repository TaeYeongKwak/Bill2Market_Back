package com.example.demo.service.chat;

import com.example.demo.model.chat.AlarmResponseDTO;
import com.example.demo.model.chat.ChatListResponseDTO;
import com.example.demo.model.chat.ChatMessageEvent;
import com.example.demo.model.chat.ChatResponseDTO;

import java.util.List;

public interface ChatService {
  
    public ChatResponseDTO findChat(Integer itemId, Integer lenterIndex);
//    public void setNonReadCount();
    public void messageSave(ChatMessageEvent chatMessageEvent);

    public List<ChatListResponseDTO> findClientChatList(Integer clientIndex);

    public List<AlarmResponseDTO> getChatAlarmList(Integer clientIndex);
}
