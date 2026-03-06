package com.goattech.gigstracker.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FixedExpenseDto(
                UUID id,
                String description,
                UUID categoryId,
                UUID vehicleId,
                BigDecimal amount,
                LocalDate startDate,
                LocalDate endDate,
                Boolean isActive,
                OffsetDateTime createdAt) {
}
