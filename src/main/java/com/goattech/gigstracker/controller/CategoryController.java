package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.CategoryDto;
import com.goattech.gigstracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Endpoints for managing expense and income categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories, optionally filtered by type")
    public List<CategoryDto> getCategories(@AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String type) {
        if (!StringUtils.hasText(type)) {
            type = "EXPENSE"; // Default type if not provided
        }
        return categoryService.getCategories(type);
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public CategoryDto createCategory(@AuthenticationPrincipal Jwt jwt, @RequestBody CategoryDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return categoryService.createCategory(dto, userId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public CategoryDto updateCategory(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody CategoryDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return categoryService.updateCategory(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }
}
