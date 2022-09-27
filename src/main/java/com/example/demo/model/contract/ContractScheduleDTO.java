package com.example.demo.model.contract;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ContractScheduleDTO {

    private Integer contractId;
    private Integer chatId;
    private Integer senderId;
    private String senderNickname;

    @QueryProjection
    public ContractScheduleDTO(Integer contractId, Integer chatId, Integer senderId, String senderNickname) {
        this.contractId = contractId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
    }
}
