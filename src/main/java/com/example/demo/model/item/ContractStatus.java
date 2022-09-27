package com.example.demo.model.item;

public enum ContractStatus{

    GENERAL(0),
    RESERVATION(1),
    RENTAL(2),
    NORESERVATION(3);

    private final int value;
    private ContractStatus(int value){
        this.value = value;
    }

    private int getValue(){
        return value;
    }

    public int getContractStatusValue(){
        ContractStatus contractStatus = ContractStatus.GENERAL;
        return contractStatus.getValue();
    }
}
