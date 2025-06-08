package com.hotel.reservation_service.controller;

import com.hotel.reservation_service.dto.GuestRequestDTO;
import com.hotel.reservation_service.dto.GuestResponseDTO;
import com.hotel.reservation_service.service.GuestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

    @Autowired
    private GuestService guestService;

    // Create a new guest with data from request body
    @PostMapping
    public ResponseEntity<GuestResponseDTO> createGuest(@Valid @RequestBody GuestRequestDTO guestRequestDTO) {
        logger.info("Creating guest: {}", guestRequestDTO);
        GuestResponseDTO response = guestService.createGuest(guestRequestDTO);
        logger.debug("Guest created with ID: {}", response.getGuestId());
        return ResponseEntity.ok(response);
    }

    // Retrieve guest details by guest ID
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResponseDTO> getGuestById(@PathVariable Long guestId) {
        logger.info("Fetching guest with ID: {}", guestId);
        return ResponseEntity.ok(guestService.getGuestById(guestId));
    }

    // Get list of all guests
    @GetMapping
    public ResponseEntity<List<GuestResponseDTO>> getAllGuests() {
        logger.info("Fetching all guests");
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    // Update existing guest information by guest ID
    @PutMapping("/{guestId}")
    public ResponseEntity<GuestResponseDTO> updateGuest(@PathVariable Long guestId, @Valid @RequestBody GuestRequestDTO guestRequestDTO) {
        logger.info("Updating guest with ID: {}", guestId);
        return ResponseEntity.ok(guestService.updateGuest(guestId, guestRequestDTO));
    }

    // Delete guest by guest ID
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        logger.info("Deleting guest with ID: {}", guestId);
        guestService.deleteGuest(guestId);
        logger.debug("Guest with ID {} deleted", guestId);
        return ResponseEntity.noContent().build(); // Return 204 No Content after deletion
    }
}
