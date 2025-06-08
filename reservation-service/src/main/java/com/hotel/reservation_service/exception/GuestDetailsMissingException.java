package com.hotel.reservation_service.exception;

public class GuestDetailsMissingException extends RuntimeException {
    public GuestDetailsMissingException(String message) {
        super(message);
    }
}
