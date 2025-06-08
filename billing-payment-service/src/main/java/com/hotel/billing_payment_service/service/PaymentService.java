package com.hotel.billing_payment_service.service;

import com.stripe.model.checkout.Session;

public interface PaymentService {
    Session createCheckoutSession(double amount, String currency, String customerName, String customerEmail) throws Exception;
    Session retrieveSession(String sessionId) throws Exception;
}
