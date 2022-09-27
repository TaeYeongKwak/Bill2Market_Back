package com.example.demo.model.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@Data
public class OpenBankClientInfo implements Serializable {

    private String state;
    private Boolean isLenter;

}
