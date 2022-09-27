package com.example.demo.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositForClientDTO {

    private Integer contractId;
    private Integer deposit;
    private Integer price;
    private Integer ownerIndex;
    private Integer lenterIndex;
    private String ownerNickname;
    private String lenterNickname;
    private String ownerFintechId;
    private String lenterFintechId;
}
