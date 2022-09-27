package com.example.demo.exception.chat;

public class ChatFileCreateFailedException extends RuntimeException{

    public ChatFileCreateFailedException(){
        super();
    }

    public ChatFileCreateFailedException(String message){
        super(message);
    }

    public ChatFileCreateFailedException(String message, Throwable th){
        super(message, th);
    }

}
