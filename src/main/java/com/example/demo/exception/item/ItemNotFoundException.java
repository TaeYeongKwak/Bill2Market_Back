package com.example.demo.exception.item;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(){
        super();
    }

    public ItemNotFoundException(String message){
        super(message);
    }

    public ItemNotFoundException(String message, Throwable th){
        super(message, th);
    }

}
