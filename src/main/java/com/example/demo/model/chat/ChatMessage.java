package com.example.demo.model.chat;

import com.example.demo.model.contract.ContractScheduleDTO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Builder
@Data
public class ChatMessage {

    public ChatMessage(){

    }

    public ChatMessage(ChatType chatType, Integer chatId, Integer senderId, String senderNickname, String message, Boolean isRead, MessageType messageType, String createDate) {
        this.chatType = chatType;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.message = message;
        this.isRead = isRead;
        this.messageType = messageType;
        this.createDate = createDate;
    }

    public enum ChatType{
        ENTER, MESSAGE, EXIT
    }

    private ChatType chatType;
    private Integer chatId;
    private Integer senderId;
    private String senderNickname;
    private String message;
    private Boolean isRead;
    private MessageType messageType;
    private String createDate;

    public void setMessageType(int type){
        this.messageType = MessageType.values()[type];
    }

    public static ChatMessage getChatMessage(ContractScheduleDTO contractScheduleDTO, MessageType messageType){
        return  ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contractScheduleDTO.getChatId())
                .senderId(contractScheduleDTO.getSenderId())
                .senderNickname(contractScheduleDTO.getSenderNickname())
                .messageType(messageType)
                .message(String.valueOf(contractScheduleDTO.getContractId()))
                .build();
    }

}
