package com.example.demo.model.bank;

import lombok.Data;

@Data
public class TransferRequestDTO {

    private Integer contractId;
    private Integer price;
    private Integer deposit;

    public Integer paymentAmount(){
        return (int)((price + deposit) * 1.05);
    }

}
