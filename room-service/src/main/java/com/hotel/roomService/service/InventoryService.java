package com.hotel.roomService.service;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;

import java.util.List;

public interface InventoryService {

    // Create a new inventory item using request DTO
    InventoryItem createItem(InventoryRequestDTO dto);

    // Get a list of all inventory items with response DTOs
    List<InventoryResponseDTO> getAllItems();

    // Get a specific inventory item by its ID
    InventoryResponseDTO getItemById(Long id);

    // Update an existing inventory item with new data
    InventoryItem updateItem(Long id, InventoryRequestDTO dto);
}
