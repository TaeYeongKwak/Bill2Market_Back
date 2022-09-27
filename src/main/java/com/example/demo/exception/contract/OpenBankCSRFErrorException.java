package com.example.demo.exception.contract;

public class OpenBankCSRFErrorException extends RuntimeException{

    public OpenBankCSRFErrorException(){
        super();
    }

    public OpenBankCSRFErrorException(String message){
        super(message);
    }

    public OpenBankCSRFErrorException(String message, Throwable th){
        super(message, th);
    }

}
