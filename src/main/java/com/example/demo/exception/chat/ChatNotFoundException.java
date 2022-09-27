package com.example.demo.exception.chat;

public class ChatNotFoundException extends RuntimeException{

    public ChatNotFoundException(){
        super();
    }

    public ChatNotFoundException(String message){
        super(message);
    }

    public ChatNotFoundException(String message, Throwable th){
        super(message, th);
    }

}
