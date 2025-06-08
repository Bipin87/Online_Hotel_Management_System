package com.hotel.staff_service.exception;

public class StaffNotFoundException extends RuntimeException{
    public StaffNotFoundException(String message){
        super(message);
    }
}
