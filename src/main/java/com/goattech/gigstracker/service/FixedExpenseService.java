package com.goattech.gigstracker.service;

import com.goattech.gigstracker.exception.BusinessException;
import com.goattech.gigstracker.model.FixedExpense;
import com.goattech.gigstracker.model.Vehicle;
import com.goattech.gigstracker.model.dto.FixedExpenseDto;
import com.goattech.gigstracker.repository.FixedExpenseRepository;
import com.goattech.gigstracker.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FixedExpenseService {

    private final FixedExpenseRepository fixedExpenseRepository;
    private final VehicleRepository vehicleRepository;

    public FixedExpenseService(FixedExpenseRepository fixedExpenseRepository, VehicleRepository vehicleRepository) {
        this.fixedExpenseRepository = fixedExpenseRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<FixedExpenseDto> getFixedExpensesForUser(UUID userId) {
        return fixedExpenseRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FixedExpenseDto createFixedExpense(FixedExpenseDto dto, UUID userId) {
        validateDuplicity(dto, userId, null);

        FixedExpense expense = new FixedExpense();
        updateEntityFromDto(expense, dto);
        expense.setUserId(userId);

        populateVehicleDescription(expense, dto);

        return toDto(fixedExpenseRepository.save(expense));
    }

    @Transactional
    public FixedExpenseDto updateFixedExpense(UUID id, FixedExpenseDto dto, UUID userId) {
        FixedExpense expense = fixedExpenseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fixed expense not found"));

        if (!expense.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        validateDuplicity(dto, userId, id);
        updateEntityFromDto(expense, dto);

        populateVehicleDescription(expense, dto);

        return toDto(fixedExpenseRepository.save(expense));
    }

    @Transactional
    public void deleteFixedExpense(UUID id, UUID userId) {
        FixedExpense expense = fixedExpenseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fixed expense not found"));

        if (!expense.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        fixedExpenseRepository.delete(expense);
    }

    private void updateEntityFromDto(FixedExpense entity, FixedExpenseDto dto) {
        if (dto.categoryId() != null) {
            entity.setCategoryId(dto.categoryId());
        }
        if (dto.vehicleId() != null) {
            entity.setVehicleId(dto.vehicleId());
        }
        if (dto.amount() != null) {
            entity.setAmount(dto.amount());
        }
        if (dto.startDate() != null) {
            entity.setStartDate(dto.startDate());
        }
        if (dto.endDate() != null) {
            entity.setEndDate(dto.endDate());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.isActive() != null) {
            entity.setActive(dto.isActive());
        }
    }

    private void validateDuplicity(FixedExpenseDto dto, UUID userId, UUID currentId) {
        if (dto.vehicleId() != null && dto.categoryId() != null) {
            fixedExpenseRepository.findByUserIdAndCategoryIdAndVehicleId(userId, dto.categoryId(), dto.vehicleId())
                    .ifPresent(expense -> {
                        if (currentId == null || !expense.getId().equals(currentId)) {
                            throw new BusinessException("This vehicle already has a fixed expense for this category");
                        }
                    });
        }
    }

    private void populateVehicleDescription(FixedExpense entity, FixedExpenseDto dto) {
        if (dto.vehicleId() != null && (dto.description() == null || dto.description().isBlank())) {
            Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                    .orElseThrow(() -> new BusinessException("Vehicle not found"));
            entity.setDescription(vehicle.getName());
        }
    }

    private FixedExpenseDto toDto(FixedExpense entity) {
        return new FixedExpenseDto(
                entity.getId(),
                entity.getDescription(),
                entity.getCategoryId(),
                entity.getVehicleId(),
                entity.getAmount(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getActive(),
                entity.getCreatedAt());
    }
}
