package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.CategoryDto;
import com.goattech.gigstracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getCategories(@AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String type) {
        if (!StringUtils.hasText(type)) {
            type = "EXPENSE"; // Default type if not provided
        }
        return categoryService.getCategories(type);
    }

    @PostMapping
    public CategoryDto createCategory(@AuthenticationPrincipal Jwt jwt, @RequestBody CategoryDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return categoryService.createCategory(dto, userId);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody CategoryDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return categoryService.updateCategory(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }
}
