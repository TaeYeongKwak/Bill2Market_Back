package com.example.demo.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Data
public class ContractIBorrowedResponseDTO {

    private Integer itemId;
    private String itemTitle;
    private Integer price;
    private Integer deposit;
    private String itemAddress;
    private String itemPhoto;
    private String contractStatus;
    private LocalDate startDate;
    private Integer reviewWrite;

}
