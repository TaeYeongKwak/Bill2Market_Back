package com.example.demo.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class AlarmResponseDTO {
    private String nickname;
    private Integer nonReadMessage;
    private String fileName;
}
