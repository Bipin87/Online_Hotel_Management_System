package com.hotel.billing_payment_service.exception;

public class BillAlreadyPaidException extends RuntimeException{
    public BillAlreadyPaidException(String message){
        super(message);
    }
}
