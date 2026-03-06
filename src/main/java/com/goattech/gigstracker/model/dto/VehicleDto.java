package com.goattech.gigstracker.model.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record VehicleDto(
        UUID id,
        String name,
        Integer year,
        String plateNumber,
        Boolean isLease,
        Boolean isActive,
        Boolean isDefault,
        OffsetDateTime createdAt) {
}
