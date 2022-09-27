package com.example.demo.exception.client;

public class ExistIdException extends RuntimeException{

    public ExistIdException(){
        super();
    }

    public ExistIdException(String message){
        super(message);
    }

    public ExistIdException(String message, Throwable th){
        super(message, th);
    }
}
