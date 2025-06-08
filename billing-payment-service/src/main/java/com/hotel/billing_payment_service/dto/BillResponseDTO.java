package com.hotel.billing_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {

    private Long billId;
    private Long bookingId;
    private String customerName;
    private String sessionId;
    private Double totalAmount;
    private String paymentMode;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private Long id;

    public BillResponseDTO(long l, long l1, String johnDoe, String session123, double v, String card, String pending, LocalDateTime now) {
    }
}
