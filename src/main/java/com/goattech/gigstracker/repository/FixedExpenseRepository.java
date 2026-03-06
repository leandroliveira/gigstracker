package com.goattech.gigstracker.repository;

import com.goattech.gigstracker.model.FixedExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FixedExpenseRepository extends JpaRepository<FixedExpense, UUID> {
    List<FixedExpense> findByUserId(UUID userId);

    Optional<FixedExpense> findByUserIdAndCategoryIdAndVehicleId(UUID userId, UUID categoryId, UUID vehicleId);
}
