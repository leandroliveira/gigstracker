package com.goattech.gigstracker.model.dto;

import java.time.Instant;
import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String type,
        boolean isActive,
        Instant createdAt) {
}
