package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.VehicleDto;
import com.goattech.gigstracker.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@Tag(name = "Vehicles", description = "Endpoints for managing user vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    @Operation(summary = "Get all vehicles for the authenticated user")
    public List<VehicleDto> getVehicles(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.getAllVehiclesForUser(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new vehicle for the authenticated user")
    public VehicleDto createVehicle(@AuthenticationPrincipal Jwt jwt, @RequestBody VehicleDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.createVehicle(dto, userId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing vehicle")
    public VehicleDto updateVehicle(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody VehicleDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.updateVehicle(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> deleteVehicle(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        vehicleService.deleteVehicle(id, userId);
        return ResponseEntity.noContent().build();
    }
}
