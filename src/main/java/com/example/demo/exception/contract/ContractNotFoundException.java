package com.example.demo.exception.contract;

public class ContractNotFoundException extends RuntimeException{

    public ContractNotFoundException(){
        super();
    }

    public ContractNotFoundException(String message){
        super(message);
    }

    public ContractNotFoundException(String message, Throwable th){
        super(message, th);
    }

}
