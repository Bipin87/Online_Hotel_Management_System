package com.hotel.billing_payment_service.repository;

import com.hotel.billing_payment_service.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Finds a Bill by Stripe session ID
    Optional<Bill> findBySessionId(String sessionId);

    // Finds a Bill by booking ID
    Optional<Bill> findByBookingId(Long bookingId);
}
