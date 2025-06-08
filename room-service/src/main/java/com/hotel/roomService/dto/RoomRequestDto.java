package com.hotel.roomService.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDto {

    @NotBlank(message = "Room number is required")
    @Positive(message = "Room number must be greater than zero")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private String roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private Double pricePerNight;

    @NotNull(message = "Room status is required")
    private Boolean available;
}
