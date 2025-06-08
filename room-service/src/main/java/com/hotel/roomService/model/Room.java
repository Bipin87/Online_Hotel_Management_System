package com.hotel.roomService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    private String roomType;  // No enum, just String like "Single", "Double", "Suite", etc.

    private boolean available; // true = available, false = booked

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItem> roomInventories = new ArrayList<>();


    @CreationTimestamp
    private Instant createdAt;
}
