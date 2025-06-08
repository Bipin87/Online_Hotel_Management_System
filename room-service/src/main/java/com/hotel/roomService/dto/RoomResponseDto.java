package com.hotel.roomService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private Boolean available;
    private Instant createdAt;
    private List<InventoryItemShortDTO> roomInventories;

}
