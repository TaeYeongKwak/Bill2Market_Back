package com.example.demo.model.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Data
public class SimpleItem {

    private Integer itemId;
    private Integer itemPhotoIndex;
    private String itemTitle;
    private String itemAddress;
    private Integer price;
    private Integer deposit;
    private String itemPhoto;
    private String contractStatus;
    private LocalDate createDate;
    private Boolean isLike;

    @QueryProjection
    public SimpleItem(Integer itemId, String itemTitle, String itemAddress, Integer price, Integer deposit, String itemPhoto, String contractStatus, LocalDate createDate, Boolean isLike) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemAddress = itemAddress;
        this.price = price;
        this.deposit = deposit;
        this.itemPhoto = itemPhoto;
        this.contractStatus = contractStatus;
        this.createDate = createDate;
        this.isLike = isLike;
    }
}
