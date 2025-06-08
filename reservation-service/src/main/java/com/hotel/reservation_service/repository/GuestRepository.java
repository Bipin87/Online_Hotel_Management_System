package com.hotel.reservation_service.repository;


import com.hotel.reservation_service.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}

