package com.exchange.exchange.exceptions;

public class AccessKeyException extends RuntimeException{

    public AccessKeyException(){
        super();
    }
    public AccessKeyException(String message) {
        super(message);
    }

}
