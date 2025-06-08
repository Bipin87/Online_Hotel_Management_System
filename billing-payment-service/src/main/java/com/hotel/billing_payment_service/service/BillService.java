package com.hotel.billing_payment_service.service;

import com.hotel.billing_payment_service.dto.BillResponseDTO;
import com.hotel.billing_payment_service.dto.PaymentResponse;

import java.util.List;

public interface BillService {

    // Creates a bill for the given booking ID and returns payment information
    PaymentResponse createBill(Long bookingId) throws Exception;

    // Retrieves a specific bill by its ID
    BillResponseDTO getBill(Long id);

    // Retrieves all bills
    List<BillResponseDTO> getAllBills();
}
