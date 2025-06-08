package com.hotel.billing_payment_service.controller;

import com.hotel.billing_payment_service.repository.BillRepository;
import com.hotel.billing_payment_service.service.imple.PaymentService;  // Keep only this import

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment") // Base path for payment-related endpoints
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillRepository billRepository;

    // Called when payment is successful, with sessionId from the payment gateway
    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam("sessionId") String sessionId) {
        logger.info("Handling payment success for sessionId: {}", sessionId);
        ResponseEntity<String> response = paymentService.handlePaymentSuccess(sessionId);
        logger.info("Payment success handled for sessionId: {}", sessionId);
        return response;
    }

    // Called when payment fails or is canceled
    @GetMapping("/failed")
    public String paymentCancel() {
        logger.warn("Payment failed or canceled by user");
        return "Your payment was not successful. Please try again later.";
    }
}
