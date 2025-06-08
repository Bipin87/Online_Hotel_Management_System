package com.hotel.reservation_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Guest detail is mandatory")
    private GuestRequestDTO guest;  // No guest ID expected

    @NotNull(message = "Check-in date is required")
    private LocalDate checkinDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkoutDate;

    @NotNull(message = "Number of guests is required")
    @Min(1)
    private Integer numGuests;
}
