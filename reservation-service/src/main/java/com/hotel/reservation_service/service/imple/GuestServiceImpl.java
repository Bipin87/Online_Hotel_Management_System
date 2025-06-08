package com.hotel.reservation_service.service.imple;

import com.hotel.reservation_service.dto.GuestRequestDTO;
import com.hotel.reservation_service.dto.GuestResponseDTO;
import com.hotel.reservation_service.exception.GuestNotFoundException;
import com.hotel.reservation_service.model.Guest;
import com.hotel.reservation_service.repository.GuestRepository;
import com.hotel.reservation_service.service.GuestService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {

    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GuestResponseDTO createGuest(GuestRequestDTO guestRequestDTO) {
        logger.info("Creating guest with email: {}", guestRequestDTO.getEmail());

        Guest guest = new Guest();
        guest.setFullName(guestRequestDTO.getFullName());
        guest.setEmail(guestRequestDTO.getEmail());
        guest.setPhone(guestRequestDTO.getPhone());
        guest.setAddress(guestRequestDTO.getAddress());
        guest.setNationality(guestRequestDTO.getNationality());
        guest.setIdProofType(guestRequestDTO.getIdProofType());
        guest.setIdProofNumber(guestRequestDTO.getIdProofNumber());

        Guest savedGuest = guestRepository.save(guest);
        logger.info("Guest created with id: {}", savedGuest.getGuestId());

        return modelMapper.map(savedGuest, GuestResponseDTO.class);
    }

    @Override
    public GuestResponseDTO getGuestById(Long guestId) {
        logger.info("Fetching guest with id: {}", guestId);
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> {
                    logger.error("Guest not found with id: {}", guestId);
                    return new GuestNotFoundException("Guest not found with id: " + guestId);
                });
        return modelMapper.map(guest, GuestResponseDTO.class);
    }

    @Override
    public List<GuestResponseDTO> getAllGuests() {
        logger.info("Fetching all guests");
        return guestRepository.findAll()
                .stream()
                .map(guest -> modelMapper.map(guest, GuestResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public GuestResponseDTO updateGuest(Long guestId, GuestRequestDTO guestRequestDTO) {
        logger.info("Updating guest with id: {}", guestId);

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> {
                    logger.error("Guest not found with id: {}", guestId);
                    return new GuestNotFoundException("Guest not found with id: " + guestId);
                });

        guest.setFullName(guestRequestDTO.getFullName());
        guest.setEmail(guestRequestDTO.getEmail());
        guest.setPhone(guestRequestDTO.getPhone());
        guest.setAddress(guestRequestDTO.getAddress());
        guest.setNationality(guestRequestDTO.getNationality());
        guest.setIdProofType(guestRequestDTO.getIdProofType());
        guest.setIdProofNumber(guestRequestDTO.getIdProofNumber());

        Guest updatedGuest = guestRepository.save(guest);
        logger.info("Guest updated with id: {}", guestId);

        return modelMapper.map(updatedGuest, GuestResponseDTO.class);
    }

    @Override
    public void deleteGuest(Long guestId) {
        logger.info("Deleting guest with id: {}", guestId);

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> {
                    logger.error("Guest not found with id: {}", guestId);
                    return new GuestNotFoundException("Guest not found with id: " + guestId);
                });

        guestRepository.delete(guest);
        logger.info("Guest deleted with id: {}", guestId);
    }
}
