package com.example.demo.model.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
@AllArgsConstructor
public class PaymentResultResponseDTO {

    private Integer contractId;
    private Integer paymentAmount;
    private String paymentTime;

}
