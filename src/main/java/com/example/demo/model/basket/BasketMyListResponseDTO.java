package com.example.demo.model.basket;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
public class BasketMyListResponseDTO {

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
