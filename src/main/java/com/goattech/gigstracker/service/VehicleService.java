package com.goattech.gigstracker.service;

import com.goattech.gigstracker.exception.BusinessException;
import com.goattech.gigstracker.model.Vehicle;
import com.goattech.gigstracker.model.dto.VehicleDto;
import com.goattech.gigstracker.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDto> getAllVehiclesForUser(UUID userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleDto createVehicle(VehicleDto dto, UUID userId) {
        validateDuplicity(dto, userId, null);

        if (dto.isDefault() != null && dto.isDefault()) {
            handleDefaultVehicle(userId);
        }

        Vehicle vehicle = new Vehicle();
        updateEntityFromDto(vehicle, dto);
        vehicle.setUserId(userId);

        return toDto(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleDto updateVehicle(UUID id, VehicleDto dto, UUID userId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vehicle not found"));

        if (!vehicle.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        validateDuplicity(dto, userId, id);

        if (dto.isDefault() != null && dto.isDefault()) {
            handleDefaultVehicle(userId);
        }

        updateEntityFromDto(vehicle, dto);

        return toDto(vehicleRepository.save(vehicle));
    }

    private void handleDefaultVehicle(UUID userId) {
        vehicleRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(v -> {
                    v.setDefault(false);
                    vehicleRepository.save(v);
                });
    }

    @Transactional
    public void deleteVehicle(UUID id, UUID userId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vehicle not found"));

        if (!vehicle.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        vehicleRepository.delete(vehicle);
    }

    private void validateDuplicity(VehicleDto dto, UUID userId, UUID currentId) {
        if (dto.name() != null) {
            vehicleRepository.findByNameAndUserId(dto.name(), userId)
                    .ifPresent(v -> {
                        if (currentId == null || !v.getId().equals(currentId)) {
                            throw new BusinessException("Vehicle name already exists for this user");
                        }
                    });
        }

        if (dto.plateNumber() != null) {
            vehicleRepository.findByPlateNumberAndUserId(dto.plateNumber(), userId)
                    .ifPresent(v -> {
                        if (currentId == null || !v.getId().equals(currentId)) {
                            throw new BusinessException("Vehicle plate number already exists for this user");
                        }
                    });
        }
    }

    private void updateEntityFromDto(Vehicle entity, VehicleDto dto) {
        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.year() != null) {
            entity.setYear(dto.year());
        }
        if (dto.plateNumber() != null) {
            entity.setPlateNumber(dto.plateNumber());
        }
        if (dto.isLease() != null) {
            entity.setLease(dto.isLease());
        }
        if (dto.isActive() != null) {
            entity.setActive(dto.isActive());
        }
        if (dto.isDefault() != null) {
            entity.setDefault(dto.isDefault());
        }
    }

    private VehicleDto toDto(Vehicle entity) {
        return new VehicleDto(
                entity.getId(),
                entity.getName(),
                entity.getYear(),
                entity.getPlateNumber(),
                entity.isLease(),
                entity.isActive(),
                entity.isDefault(),
                entity.getCreatedAt());
    }
}
