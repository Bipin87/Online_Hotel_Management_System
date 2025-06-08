package com.hotel.billing_payment_service.controller;

import com.hotel.billing_payment_service.dto.BillResponseDTO;
import com.hotel.billing_payment_service.dto.PaymentResponse;
import com.hotel.billing_payment_service.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bills") // Base URL for all bill-related endpoints
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    private final BillService billService;

    // Constructor injection of BillService
    public BillController(BillService billService) {
        this.billService = billService;
    }

    // Endpoint to generate a new bill based on bookingId
    @GetMapping("/generate")
    public ResponseEntity<PaymentResponse> createBill(@RequestParam Long bookingId) throws Exception {
        logger.info("Received request to generate bill for bookingId: {}", bookingId);
        PaymentResponse paymentResponse = billService.createBill(bookingId);
        logger.info("Bill generated successfully for bookingId: {}", bookingId);
        return ResponseEntity.ok(paymentResponse);
    }

    // Endpoint to fetch a specific bill by its ID
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBill(@PathVariable Long id) {
        logger.info("Fetching bill with id: {}", id);
        BillResponseDTO billResponse = billService.getBill(id);
        logger.info("Bill fetched successfully with id: {}", id);
        return ResponseEntity.ok(billResponse);
    }

    // Endpoint to fetch all bills
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        logger.info("Fetching all bills");
        List<BillResponseDTO> bills = billService.getAllBills();
        logger.info("Fetched {} bills", bills.size());
        return ResponseEntity.ok(bills);
    }
}
