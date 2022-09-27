package com.example.demo.model.item;

import lombok.Data;

@Data
public class ItemSearchRequestDTO {

    private Integer categoryBig;
    private Integer categoryMiddle;
    private Integer categorySmall;
    private String query;
    private Double longitude;
    private Double latitude;
    private Integer page;
    private OrderType orderType;

}