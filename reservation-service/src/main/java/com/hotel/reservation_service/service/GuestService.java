package com.hotel.reservation_service.service;

import com.hotel.reservation_service.dto.GuestRequestDTO;
import com.hotel.reservation_service.dto.GuestResponseDTO;

import java.util.List;

public interface GuestService {

    // Create a new guest record
    GuestResponseDTO createGuest(GuestRequestDTO guestRequestDTO);

    // Retrieve guest details by guest ID
    GuestResponseDTO getGuestById(Long guestId);

    // Retrieve list of all guests
    List<GuestResponseDTO> getAllGuests();

    // Update guest details for a given guest ID
    GuestResponseDTO updateGuest(Long guestId, GuestRequestDTO guestRequestDTO);

    // Delete a guest record by guest ID
    void deleteGuest(Long guestId);
}
