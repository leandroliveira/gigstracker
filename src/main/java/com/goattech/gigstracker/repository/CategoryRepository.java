package com.goattech.gigstracker.repository;

import com.goattech.gigstracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByTypeAndIsActiveTrue(String type);

    Optional<Category> findByNameAndUserIdIsNull(String name);

    Optional<Category> findByNameAndUserId(String name, UUID userId);

}
