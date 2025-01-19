package com.exchange.exchange.exceptions;

public class AccessRestrictedException extends RuntimeException{

    public AccessRestrictedException(){
        super();
    }
    public AccessRestrictedException(String message) {
        super(message);
    }

}
