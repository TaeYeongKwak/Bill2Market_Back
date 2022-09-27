package com.example.demo.exception.contract;

public class ContractBillPayStatusErrorException extends RuntimeException{

    public ContractBillPayStatusErrorException(){
        super();
    }

    public ContractBillPayStatusErrorException(String message){
        super(message);
    }

    public ContractBillPayStatusErrorException(String message, Throwable th){
        super(message, th);
    }

}
