package com.hotel.billing_payment_service.client;

import com.hotel.billing_payment_service.dto.BookingResponseDTO;

public interface BookingServiceClient {
    BookingResponseDTO getBooking(Long bookingId);
}
