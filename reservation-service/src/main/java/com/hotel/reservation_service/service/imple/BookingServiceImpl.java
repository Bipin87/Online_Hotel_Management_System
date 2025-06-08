package com.hotel.reservation_service.service.imple;

import com.hotel.reservation_service.dto.*;
import com.hotel.reservation_service.exception.*;
import com.hotel.reservation_service.model.Booking;
import com.hotel.reservation_service.model.BookingStatus;
import com.hotel.reservation_service.model.Guest;
import com.hotel.reservation_service.repository.BookingRepository;
import com.hotel.reservation_service.repository.GuestRepository;
import com.hotel.reservation_service.service.BookingService;
import com.hotel.reservation_service.service.GuestService;
import com.hotel.reservation_service.service.RoomServiceClient;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomServiceClient roomServiceClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public BookingResponseDTO createBooking(String roomType, BookingRequestDTO bookingRequestDTO) {
        logger.info("Creating booking for roomType: {}", roomType);

        RoomResponseDto room = roomServiceClient.getAvailableRoomByType(roomType);
        if (room == null || !room.isAvailable()) {
            logger.error("No available room of type: {}", roomType);
            throw new RoomNotAvailableException("No available room of type: " + roomType);
        }

        GuestRequestDTO guestDetail = bookingRequestDTO.getGuest();
        if (guestDetail == null) {
            logger.error("Guest details are missing in booking request");
            throw new GuestDetailsMissingException("Guest details are missing.");
        }

        GuestResponseDTO guestResponse = guestService.createGuest(guestDetail);
        Guest guestCreated = modelMapper.map(guestResponse, Guest.class);

        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());
        booking.setGuestId(guestCreated.getGuestId());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCheckinDate(bookingRequestDTO.getCheckinDate());
        booking.setCheckoutDate(bookingRequestDTO.getCheckoutDate());
        booking.setNumGuests(bookingRequestDTO.getNumGuests());
        booking.setCreatedAt(LocalDateTime.now());

        roomServiceClient.updateRoomAvailability(room.getRoomId(), false);
        room.setAvailable(false);

        bookingRepository.save(booking);
        logger.info("Booking created with id: {}", booking.getBookingId());

        return new BookingResponseDTO(
                booking.getBookingId(),
                guestResponse,
                room,
                booking.getCheckinDate(),
                booking.getCheckoutDate(),
                booking.getNumGuests(),
                "PENDING"
        );
    }

    @Override
    public BookingResponseDTO getBookingById(Long bookingId) {
        logger.info("Fetching booking with id: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new BookingNotFoundException("Booking not found with id: " + bookingId);
                });

        Guest guest = guestRepository.findById(booking.getGuestId())
                .orElseThrow(() -> {
                    logger.error("Guest not found for booking id: {}", bookingId);
                    return new GuestNotFoundException("Guest not found.");
                });

        RoomResponseDto room = roomServiceClient.getRoomById(booking.getRoomId());

        return new BookingResponseDTO(
                booking.getBookingId(),
                modelMapper.map(guest, GuestResponseDTO.class),
                room,
                booking.getCheckinDate(),
                booking.getCheckoutDate(),
                booking.getNumGuests(),
                booking.getBookingStatus().name()
        );
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepository.findAll().stream().map(booking -> {
            Guest guest = guestRepository.findById(booking.getGuestId())
                    .orElseThrow(() -> {
                        logger.error("Guest not found for booking id: {}", booking.getBookingId());
                        return new GuestNotFoundException("Guest not found.");
                    });

            RoomResponseDto room = roomServiceClient.getRoomById(booking.getRoomId());

            return new BookingResponseDTO(
                    booking.getBookingId(),
                    modelMapper.map(guest, GuestResponseDTO.class),
                    room,
                    booking.getCheckinDate(),
                    booking.getCheckoutDate(),
                    booking.getNumGuests(),
                    booking.getBookingStatus().name()
            );
        }).collect(Collectors.toList());
    }

    public void updateBookingStatus(Long bookingId) {
        logger.info("Updating booking status to BOOKED for booking id: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found for id: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found for ID: " + bookingId);
                });

        booking.setBookingStatus(BookingStatus.BOOKED);
        bookingRepository.save(booking);
        logger.info("Booking status updated to BOOKED for id: {}", bookingId);
    }

    @Override
    public BookingResponseDTO updateBooking(Long bookingId, @Valid BookingRequestDTO bookingRequestDTO) {
        logger.info("Updating booking with id: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new BookingNotFoundException("Booking not found with id: " + bookingId);
                });

        booking.setCheckinDate(bookingRequestDTO.getCheckinDate());
        booking.setCheckoutDate(bookingRequestDTO.getCheckoutDate());
        booking.setNumGuests(bookingRequestDTO.getNumGuests());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking updated = bookingRepository.save(booking);

        Guest guest = guestRepository.findById(booking.getGuestId())
                .orElseThrow(() -> {
                    logger.error("Guest not found for booking id: {}", bookingId);
                    return new GuestNotFoundException("Guest not found.");
                });

        RoomResponseDto room = roomServiceClient.getRoomById(updated.getRoomId());

        logger.info("Booking updated with id: {}", bookingId);
        return new BookingResponseDTO(
                updated.getBookingId(),
                modelMapper.map(guest, GuestResponseDTO.class),
                room,
                updated.getCheckinDate(),
                updated.getCheckoutDate(),
                updated.getNumGuests(),
                updated.getBookingStatus().name()
        );
    }

    @Override
    public void deleteBooking(Long bookingId) {
        logger.info("Deleting booking with id: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new BookingNotFoundException("Booking not found with id: " + bookingId);
                });
        bookingRepository.delete(booking);
        logger.info("Booking deleted with id: {}", bookingId);
    }
}
