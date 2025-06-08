package com.hotel.roomService.controller;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.service.RoomService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms") // Base URL for all room-related APIs
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    // Create a new room
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody RoomRequestDto roomRequestDto) {
        logger.info("Creating new room with number: {}", roomRequestDto.getRoomNumber());
        return ResponseEntity.ok(roomService.createRoom(roomRequestDto));
    }


    // Get room details by room ID
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long roomId) {
        logger.info("Fetching room with ID: {}", roomId);
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    // Get available room by room type (used while booking)
    @GetMapping("/available")
    public ResponseEntity<RoomResponseDto> getRoomByType(@RequestParam String roomType) {
        logger.info("Finding available room of type: {}", roomType);
        RoomResponseDto room = roomService.findRoomByType(roomType);
        return ResponseEntity.ok(room);
    }

    // Get available room by room type (used while booking)
    @GetMapping("/availableRooms")
    public ResponseEntity<RoomResponseDto> getAvailableRoomByType(@RequestParam String roomType) {
        logger.info("Finding available room (strict) of type: {}", roomType);
        RoomResponseDto room = roomService.findAvailableRoomByType(roomType);
        return ResponseEntity.ok(room);
    }

    // Update room availability (true or false)
    @PutMapping("/{roomId}/availability")
    public ResponseEntity<Void> updateRoomAvailability(@PathVariable Long roomId, @RequestParam boolean available) {
        logger.info("Updating availability of room ID: {} to {}", roomId, available);
        roomService.updateRoomAvailability(roomId, available);
        return ResponseEntity.ok().build();
    }

    // Get list of all rooms
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        logger.info("Fetching all rooms");
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Update room details by room ID
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long roomId, @Valid @RequestBody RoomRequestDto roomRequestDto) {
        logger.info("Updating room with ID: {}", roomId);
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomRequestDto));
    }
}
