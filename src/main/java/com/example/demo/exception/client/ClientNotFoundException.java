package com.example.demo.exception.client;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(){
        super();
    }

    public ClientNotFoundException(String message){
        super(message);
    }

    public ClientNotFoundException(String message, Throwable th){
        super(message, th);
    }

}
