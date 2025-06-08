package com.hotel.staff_service.controller;

import com.hotel.staff_service.dto.StaffRequestDTO;
import com.hotel.staff_service.dto.StaffResponseDTO;
import com.hotel.staff_service.service.StaffService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    @PostMapping
    public ResponseEntity<StaffResponseDTO> createStaff(@Valid @RequestBody StaffRequestDTO staffRequestDTO) {
        logger.info("Creating staff: {}", staffRequestDTO);
        return ResponseEntity.ok(staffService.createStaff(staffRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable Long id) {
        logger.info("Fetching staff with ID: {}", id);
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        logger.info("Fetching all staff");
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffRequestDTO staffRequestDTO) {
        logger.info("Updating staff with ID: {} with data: {}", id, staffRequestDTO);
        return ResponseEntity.ok(staffService.updateStaff(id, staffRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        logger.info("Deleting staff with ID: {}", id);
        staffService.deleteStaff(id);
        return ResponseEntity.ok("Staff deleted successfully");
    }
}
