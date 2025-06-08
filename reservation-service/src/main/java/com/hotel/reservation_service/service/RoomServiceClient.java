package com.hotel.reservation_service.service;

import com.hotel.reservation_service.dto.RoomResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "room-service") // Feign client to communicate with Room Service microservice
public interface RoomServiceClient {

    // Fetch an available room by room type
    @GetMapping("/api/rooms/available")
    RoomResponseDto getAvailableRoomByType(@RequestParam("roomType") String roomType);

    // Update the availability status of a room by roomId
    @PutMapping("/api/rooms/{roomId}/availability")
    void updateRoomAvailability(@PathVariable("roomId") Long roomId, @RequestParam("available") boolean available);

    // Get room details by roomId
    @GetMapping("/api/rooms/{roomId}")
    RoomResponseDto getRoomById(@PathVariable("roomId") Long roomId);

}
