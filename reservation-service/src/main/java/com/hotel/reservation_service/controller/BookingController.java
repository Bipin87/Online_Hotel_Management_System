package com.hotel.reservation_service.controller;

import com.hotel.reservation_service.dto.BookingRequestDTO;
import com.hotel.reservation_service.dto.BookingResponseDTO;
import com.hotel.reservation_service.service.BookingService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallbackCreateBooking")
    public ResponseEntity<?> createBooking(@RequestParam String roomType, @Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        logger.info("Creating booking for roomType: {}", roomType);
        BookingResponseDTO savedBooking = bookingService.createBooking(roomType, bookingRequestDTO);
        logger.debug("Booking created with ID: {}", savedBooking.getBookingId());
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    public ResponseEntity<String> fallbackCreateBooking(String roomType, BookingRequestDTO bookingRequestDTO, FeignException t) {
        logger.error("Fallback: Unable to create booking due to remote service failure - {}", t.getMessage());
        return ResponseEntity.status(503)
                .body("Booking service is currently unavailable. Please try again later.");
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackGetBooking")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        logger.info("Fetching booking by ID: {}", id);
        BookingResponseDTO bookingResponseDTO = bookingService.getBookingById(id);
        logger.debug("Fetched booking details: {}", bookingResponseDTO);
        return ResponseEntity.ok(bookingResponseDTO);
    }

    public ResponseEntity<String> fallbackGetBooking(Long id, FeignException t) {
        logger.error("Fallback: Unable to fetch booking by ID: {} - {}", id, t.getMessage());
        return ResponseEntity.status(503)
                .body("Booking service is currently unavailable. Unable to retrieve booking details. Please try again later.");
    }

    @GetMapping
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackGetAllBookings")
    public ResponseEntity<List<?>> getAllBookings() {
        logger.info("Fetching all bookings");
        List<?> bookings = bookingService.getAllBookings();
        logger.debug("Number of bookings fetched: {}", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/available")
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackGetAllBookings")
    public ResponseEntity<List<?>> getAllBooking() {
        logger.info("Fetching all available bookings");
        List<?> bookings = bookingService.getAllBookings();
        logger.debug("Number of available bookings fetched: {}", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    public ResponseEntity<String> fallbackGetAllBookings(FeignException t) {
        logger.error("Fallback: Unable to fetch all bookings - {}", t.getMessage());
        return ResponseEntity.status(503)
                .body("Booking service is currently unavailable. Please try again later.");
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackUpdateBooking")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        logger.info("Updating booking ID: {}", id);
        Object updatedBooking = bookingService.updateBooking(id, bookingRequestDTO);
        logger.debug("Updated booking details: {}", updatedBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    public ResponseEntity<String> fallbackUpdateBooking(Long id, BookingRequestDTO requestDTO, FeignException t) {
        logger.error("Fallback: Unable to update booking ID: {} - {}", id, t.getMessage());
        return ResponseEntity.status(503)
                .body("Booking service is currently unavailable. Unable to update booking at the moment. Please try again later.");
    }

    @PutMapping("/update-status/{bookingId}")
    public ResponseEntity<String> updateBookingStatus(@PathVariable Long bookingId) {
        logger.info("Updating booking status for ID: {}", bookingId);
        try {
            bookingService.updateBookingStatus(bookingId);
            logger.info("Booking status updated successfully for ID: {}", bookingId);
            return ResponseEntity.ok("Booking status updated successfully");
        } catch (Exception e) {
            logger.error("Error updating booking status for ID: {} - {}", bookingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating booking status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        logger.info("Deleting booking with ID: {}", id);
        bookingService.deleteBooking(id);
        logger.info("Booking deleted successfully with ID: {}", id);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
