package com.example.demo.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ContractMessage {

    private Integer contractId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public String toString() {
        return "{" +
                "\"contractId\" : \"" + contractId +
                "\", \"startDate\" : \"" + startDate +
                "\", \"endDate\" : \"" + endDate +
                "\"}";
    }
}
