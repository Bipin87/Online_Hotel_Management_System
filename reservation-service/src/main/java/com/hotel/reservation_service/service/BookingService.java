package com.hotel.reservation_service.service;

import com.hotel.reservation_service.dto.BookingRequestDTO;
import com.hotel.reservation_service.dto.BookingResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface BookingService {

    // Create a new booking with given room type and booking details
    BookingResponseDTO createBooking(String roomType, BookingRequestDTO bookingRequestDTO);

    // Retrieve booking details by booking ID
    BookingResponseDTO getBookingById(Long bookingId);

    // Retrieve all bookings in the system
    List<BookingResponseDTO> getAllBookings();

    // Update booking details for a given booking ID
    BookingResponseDTO updateBooking(Long bookingId, com.hotel.reservation_service.dto.@Valid BookingRequestDTO bookingRequestDTO);

    // Delete a booking by its ID
    void deleteBooking(Long bookingId);

    // Update the status of a booking (e.g., from PENDING to BOOKED)
    void updateBookingStatus(Long bookingId);
}
