package com.example.demo.model.contract;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractChatRequestDTO {

    private Integer chatId;
    private Integer price;
    private Integer deposit;
    private String startDate;
    private String endDate;

}
