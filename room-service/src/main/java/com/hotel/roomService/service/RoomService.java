package com.hotel.roomService.service;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.model.InventoryItem;

import java.util.List;

public interface RoomService {

    // Create a new room using the provided room data
    RoomResponseDto createRoom(RoomRequestDto roomRequestDto);

    // Retrieve room details by its unique ID
    RoomResponseDto getRoomById(Long roomId);

    // Retrieve a list of all rooms with their details
    List<RoomResponseDto> getAllRooms();

    // Find an available room by room type (e.g., Deluxe, Suite)
    RoomResponseDto findRoomByType(String roomType);

    RoomResponseDto findAvailableRoomByType(String roomType);

    // Update the availability status of a room (available or not)
    void updateRoomAvailability(Long roomId, boolean isAvailable);

    // Update room details by room ID using new data
    RoomResponseDto updateRoom(Long roomId, RoomRequestDto roomRequestDto);

    // Add a new inventory item to a specific room
    void addInventoryToRoom(Long roomId, InventoryItem itemData);
}
