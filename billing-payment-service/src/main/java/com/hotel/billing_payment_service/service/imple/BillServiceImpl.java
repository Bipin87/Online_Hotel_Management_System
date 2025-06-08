package com.hotel.billing_payment_service.service.imple;

import com.hotel.billing_payment_service.dto.BillResponseDTO;
import com.hotel.billing_payment_service.dto.BookingResponseDTO;
import com.hotel.billing_payment_service.dto.PaymentResponse;
import com.hotel.billing_payment_service.entity.Bill;
import com.hotel.billing_payment_service.exception.BillAlreadyPaidException;
import com.hotel.billing_payment_service.exception.ResourceNotFoundException;
import com.hotel.billing_payment_service.repository.BillRepository;
import com.hotel.billing_payment_service.service.BillService;
import com.hotel.billing_payment_service.service.BookingServiceClient;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Override
    public PaymentResponse createBill(Long bookingId) throws Exception {
        log.info("Creating bill for booking ID: {}", bookingId);

        BookingResponseDTO booking = bookingServiceClient.getBooking(bookingId);

        Optional<Bill> existingBillOpt = billRepository.findByBookingId(bookingId);

        if (existingBillOpt.isPresent()) {
            Bill existingBill = existingBillOpt.get();

            if ("SUCCESS".equalsIgnoreCase(existingBill.getPaymentStatus())) {
                log.warn("Bill already paid for booking ID: {}", bookingId);
                throw new BillAlreadyPaidException("Bill is already paid for booking ID: " + bookingId);
            }

            if (existingBill.getSessionId() != null) {
                log.info("Existing payment session found for booking ID: {}", bookingId);

                Session existingSession = paymentService.retrieveSession(existingBill.getSessionId());

                if ("expired".equalsIgnoreCase(existingSession.getStatus()) ||
                        "canceled".equalsIgnoreCase(existingSession.getStatus())) {
                    existingBill.setPaymentStatus("FAILED");
                    billRepository.save(existingBill);
                    log.warn("Payment session expired or canceled for booking ID: {}", bookingId);
                    throw new Exception("Payment session expired. Please create a new payment.");
                }

                // Return existing session info but create new session URL?
                // Your original code creates new session here, but also returns old sessionId?
                // You might want to clarify the flow, assuming creating new session here:

                Session session = paymentService.createCheckoutSession(
                        existingBill.getTotalAmount(),
                        "INR",
                        existingBill.getCustomerName(),
                        booking.getGuest().getEmail());

                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setCustomerName(existingBill.getCustomerName());
                paymentResponse.setSessionId(session.getId());  // Use new session id
                paymentResponse.setSessionUrl(session.getUrl());

                log.info("New payment session created for existing bill for booking ID: {}", bookingId);
                existingBill.setSessionId(session.getId());
                billRepository.save(existingBill);

                return paymentResponse;
            }
        }

        LocalDate checkInDate = booking.getCheckinDate();
        LocalDate checkOutDate = booking.getCheckoutDate();
        int totalDays = checkOutDate.getDayOfYear() - checkInDate.getDayOfYear();
        if (totalDays <= 0) totalDays = 1; // fallback if check-out same or before check-in

        Bill bill = new Bill();
        bill.setBookingId(booking.getBookingId());
        bill.setCustomerName(booking.getGuest().getFullName());
        bill.setTotalAmount(booking.getRoom().getPricePerNight() * totalDays);
        bill.setPaymentMode("CARD");
        bill.setPaymentStatus("PENDING");
        bill.setPaymentDate(LocalDateTime.now());

        Session session = paymentService.createCheckoutSession(
                bill.getTotalAmount(), "INR", bill.getCustomerName(), booking.getGuest().getEmail());

        bill.setSessionId(session.getId());
        billRepository.save(bill);

        log.info("New bill created and payment session started for booking ID: {}", bookingId);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCustomerName(booking.getGuest().getFullName());
        paymentResponse.setSessionId(session.getId());
        paymentResponse.setSessionUrl(session.getUrl());

        return paymentResponse;
    }

    @Override
    public BillResponseDTO getBill(Long id) {
        log.info("Fetching bill with id: {}", id);
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        return modelMapper.map(bill, BillResponseDTO.class);
    }

    @Override
    public List<BillResponseDTO> getAllBills() {
        log.info("Fetching all bills");
        return billRepository.findAll()
                .stream()
                .map(bill -> modelMapper.map(bill, BillResponseDTO.class))
                .collect(Collectors.toList());
    }
}
