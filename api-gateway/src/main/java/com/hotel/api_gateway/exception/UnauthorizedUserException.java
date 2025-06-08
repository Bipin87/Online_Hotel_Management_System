package com.hotel.api_gateway.exception;

public class UnauthorizedUserException extends RuntimeException{

    public UnauthorizedUserException(String message){
        super(message);
    }
}
