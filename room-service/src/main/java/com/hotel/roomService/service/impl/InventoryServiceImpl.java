package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.InventoryRepository;
import com.hotel.roomService.repository.RoomRepository;
import com.hotel.roomService.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Create a new inventory item and assign it to a room
    @Override
    public InventoryItem createItem(InventoryRequestDTO dto) {
        logger.info("Creating inventory item: {}", dto.getItemName());
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> {
                    logger.error("Room not found with id {}", dto.getRoomId());
                    return new ResourceNotFoundException("Room not found with id " + dto.getRoomId());
                });

        InventoryItem item = new InventoryItem();
        item.setItemName(dto.getItemName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setRoom(room);

        InventoryItem savedItem = inventoryRepository.save(item);
        logger.info("Inventory item created with id {}", savedItem.getId());
        return savedItem;
    }

    // Retrieve all inventory items with room info
    @Override
    public List<InventoryResponseDTO> getAllItems() {
        logger.info("Fetching all inventory items");
        return inventoryRepository.findAll().stream().map(item -> {
            InventoryResponseDTO dto = new InventoryResponseDTO();
            dto.setId(item.getId());
            dto.setItemName(item.getItemName());
            dto.setCategory(item.getCategory());
            dto.setQuantity(item.getQuantity());
            dto.setRoomId(item.getRoom().getRoomId());
            return dto;
        }).collect(Collectors.toList());
    }

    // Get a single inventory item by its ID
    @Override
    public InventoryResponseDTO getItemById(Long id) {
        logger.info("Fetching inventory item with id {}", id);
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Inventory item not found with id {}", id);
                    return new ResourceNotFoundException("Inventory item not found with id " + id);
                });

        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setRoomId(item.getRoom().getRoomId());

        return dto;
    }

    // Update an existing inventory item
    @Override
    public InventoryItem updateItem(Long id, InventoryRequestDTO dto) {
        logger.info("Updating inventory item with id {}", id);
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Inventory item not found with id {}", id);
                    return new ResourceNotFoundException("Inventory item not found with id " + id);
                });

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> {
                    logger.error("Room not found with id {}", dto.getRoomId());
                    return new ResourceNotFoundException("Room not found with id " + dto.getRoomId());
                });

        item.setItemName(dto.getItemName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setRoom(room);

        InventoryItem updatedItem = inventoryRepository.save(item);
        logger.info("Inventory item updated with id {}", updatedItem.getId());
        return updatedItem;
    }
}
