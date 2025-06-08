package com.hotel.roomService.controller;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory") // Base path for inventory APIs
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    // Constructor injection for InventoryService
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Endpoint to create a new inventory item
    @PostMapping
    public InventoryItem createItem(@RequestBody InventoryRequestDTO dto) {
        logger.info("Creating new inventory item: {}", dto.getItemName());
        return inventoryService.createItem(dto);
    }

    // Endpoint to get a list of all inventory items
    @GetMapping
    public List<InventoryResponseDTO> getAllItems() {
        logger.info("Fetching all inventory items");
        return inventoryService.getAllItems();
    }

    // Endpoint to get a specific inventory item by its ID
    @GetMapping("/{id}")
    public InventoryResponseDTO getItem(@PathVariable Long id) {
        logger.info("Fetching inventory item with ID: {}", id);
        return inventoryService.getItemById(id);
    }

    // Endpoint to update an existing inventory item by ID
    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody InventoryRequestDTO dto) {
        logger.info("Updating inventory item with ID: {}", id);
        return inventoryService.updateItem(id, dto);
    }
}
