package com.example.demo.exception.contract;

public class OpenBankTransferErrorException extends RuntimeException{

    public OpenBankTransferErrorException(){
        super();
    }

    public OpenBankTransferErrorException(String message){
        super(message);
    }

    public OpenBankTransferErrorException(String message, Throwable th){
        super(message, th);
    }
}
