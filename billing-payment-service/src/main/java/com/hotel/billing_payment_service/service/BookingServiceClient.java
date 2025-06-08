package com.hotel.billing_payment_service.service;

import com.hotel.billing_payment_service.dto.BookingResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// Ensure 'reservation-service' matches the registered Eureka service name
@FeignClient(name = "reservation-service")
public interface BookingServiceClient {

    @GetMapping("/api/bookings/{bookingId}")
    BookingResponseDTO getBooking(@PathVariable("bookingId") Long bookingId);

    @PutMapping("/api/bookings/{bookingId}/status")
    void updateBookingStatus(@PathVariable("bookingId") @NotNull Long bookingId);
}
