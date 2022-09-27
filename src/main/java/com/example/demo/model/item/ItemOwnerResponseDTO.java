package com.example.demo.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Data
public class ItemOwnerResponseDTO {

    private Integer ownerId;
    private String itemTitle;
    private Integer price;
    private Integer deposit;
    private String itemAddress;
    private int contractStatus;
    private LocalDate createDate;
    private boolean is_main;
    private String itemPhoto;
    private Integer itemId;

}