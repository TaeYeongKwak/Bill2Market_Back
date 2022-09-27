package com.example.demo.exception.client;

public class InputNullException extends RuntimeException{

    public InputNullException(){
        super();
    }

    public InputNullException(String message){
        super(message);
    }

    public InputNullException(String message, Throwable th){
        super(message, th);
    }
}
