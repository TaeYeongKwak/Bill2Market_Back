package com.example.demo.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Data
public class ItemMeListResponseDTO {

    private Integer itemId;
    private String itemTitle;
    private Integer price;
    private Integer deposit;
    private String itemAddress;
    private String contractStatus;
    private LocalDate createDate;
    private String itemPhoto;
    private Boolean isMain;

}
