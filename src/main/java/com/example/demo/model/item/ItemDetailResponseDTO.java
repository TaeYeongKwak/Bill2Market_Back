package com.example.demo.model.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
@Data
public class ItemDetailResponseDTO {

    private OwnerInfo ownerInfo;
    private Item item;
    private Long basketCount;
    private Boolean isLike;

}