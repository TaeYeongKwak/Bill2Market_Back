package com.example.demo.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class ChatResponseDTO {

    private Chat chat;
    private Integer lastUser;
    private Integer nonReadCount;

}
