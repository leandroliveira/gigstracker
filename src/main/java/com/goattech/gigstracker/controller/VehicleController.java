package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.VehicleDto;
import com.goattech.gigstracker.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDto> getVehicles(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.getAllVehiclesForUser(userId);
    }

    @PostMapping
    public VehicleDto createVehicle(@AuthenticationPrincipal Jwt jwt, @RequestBody VehicleDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.createVehicle(dto, userId);
    }

    @PutMapping("/{id}")
    public VehicleDto updateVehicle(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody VehicleDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return vehicleService.updateVehicle(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        vehicleService.deleteVehicle(id, userId);
        return ResponseEntity.noContent().build();
    }
}
