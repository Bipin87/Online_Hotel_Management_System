package com.hotel.billing_payment_service.service.imple;

import com.hotel.billing_payment_service.dto.BookingResponseDTO;
import com.hotel.billing_payment_service.entity.Bill;
import com.hotel.billing_payment_service.repository.BillRepository;
import com.hotel.billing_payment_service.service.BookingServiceClient;
import com.hotel.billing_payment_service.service.NotificationServiceClient;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final String PAYMENT_SUCCESS_STATUS = "paid";
    private static final String BILL_PAYMENT_STATUS_SUCCESS = "SUCCESS";
    private static final String CURRENCY_INR = "INR";
    private static final String SHIPPING_COUNTRY_CODE = "IN";
    private static final String SUCCESS_URL = "http://localhost:8091/api/payment/success?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL = "http://localhost:8091/api/payment/failed?sessionId={CHECKOUT_SESSION_ID}";

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    public Session createCheckoutSession(Double amount, String currency, String customerName, String email) throws Exception {
        if (amount == null) throw new IllegalArgumentException("Amount must not be null");
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .setCustomerEmail(email)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount((long) (amount * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(customerName)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setCustomerCreation(SessionCreateParams.CustomerCreation.ALWAYS)
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.valueOf(SHIPPING_COUNTRY_CODE))
                                .build()
                )
                .build();

        Session session = Session.create(params);
        log.info("Created Stripe Session ID: {}", session.getId());
        return session;
    }

    public Session retrieveSession(String sessionId) throws Exception {
        Session session = Session.retrieve(sessionId);
        log.info("Retrieved Session ID: {}, Payment Status: {}", session.getId(), session.getPaymentStatus());
        return session;
    }

    public ResponseEntity<String> handlePaymentSuccess(String sessionId) {
        try {
            Session session = retrieveSession(sessionId);

            if (PAYMENT_SUCCESS_STATUS.equalsIgnoreCase(session.getPaymentStatus())) {
                Optional<Bill> billOptional = billRepository.findBySessionId(sessionId);

                if (billOptional.isPresent()) {
                    Bill bill = billOptional.get();

                    bill.setPaymentStatus(BILL_PAYMENT_STATUS_SUCCESS);
                    bill.setPaymentDate(LocalDateTime.now());
                    billRepository.save(bill);

                    BookingResponseDTO bookingDetails = bookingServiceClient.getBooking(bill.getBookingId());

                    // Using a formatted message for notification (you may prefer JSON or DTO)
                    String customMessage = String.join("|",
                            bookingDetails.getGuest().getEmail(),
                            bill.getBookingId().toString(),
                            bookingDetails.getGuest().getFullName(),
                            bookingDetails.getRoom().getRoomType(),
                            String.valueOf(bookingDetails.getNumGuests()),
                            bookingDetails.getCheckinDate().toString(),
                            bookingDetails.getCheckoutDate().toString(),
                            bill.getTotalAmount() + " " + session.getCurrency()
                    );

                    notificationServiceClient.sendNotification(customMessage);

                    bookingServiceClient.updateBookingStatus(bill.getBookingId());

                    return ResponseEntity.ok("Payment successful. Bill status updated.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Bill not found for session ID: " + sessionId);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment not completed for session ID: " + sessionId);
            }

        } catch (Exception e) {
            log.error("Error processing payment success for sessionId {}: {}", sessionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment success: " + e.getMessage());
        }
    }
}
