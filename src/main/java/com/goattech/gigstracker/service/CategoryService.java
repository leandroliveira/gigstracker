package com.goattech.gigstracker.service;

import com.goattech.gigstracker.exception.BusinessException;
import com.goattech.gigstracker.model.Category;
import com.goattech.gigstracker.model.dto.CategoryDto;
import com.goattech.gigstracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getCategories(String type) {
        // RLS handles row filtering for user_id
        return categoryRepository.findByTypeAndIsActiveTrue(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto dto, UUID userId) {
        if (userId == null) {
            throw new BusinessException("User ID is required");
        }

        validateCategoryName(dto, userId);

        Category category = new Category();
        category.setName(dto.name());
        category.setType(dto.type());
        category.setUserId(userId);
        category.setActive(true);

        return toDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryDto dto, UUID userId) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        validateOwnership(existingCategory, userId);

        validateCategoryName(dto, userId);

        existingCategory.setName(dto.name());
        existingCategory.setType(dto.type());

        return toDto(categoryRepository.save(existingCategory));
    }

    @Transactional
    public void deleteCategory(UUID id, UUID userId) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        validateOwnership(existingCategory, userId);

        categoryRepository.delete(existingCategory);
    }

    private void validateOwnership(Category category, UUID userId) {
        if (category.getUserId() == null) {
            throw new BusinessException("Cannot modify system categories");
        }
        if (!category.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }
    }

    private void validateCategoryName(CategoryDto dto, UUID userId) {
        categoryRepository.findByNameAndUserIdIsNull(dto.name())
                .ifPresent(c -> {
                    throw new BusinessException("category is already defined by system");
                });

        categoryRepository.findByNameAndUserId(dto.name(), userId)
                .ifPresent(c -> {
                    throw new BusinessException("Category name already exists");
                });
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isActive(),
                category.getCreatedAt());
    }
}
