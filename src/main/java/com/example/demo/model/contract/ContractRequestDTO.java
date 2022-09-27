package com.example.demo.model.contract;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ContractRequestDTO {

    private Integer chatId;
    private String startDate;
    private String endDate;
    private Integer contractId;
    private ContractType contractType;

    public void setContractStatus(int type){
        this.contractType = ContractType.values()[type];
    }
}
