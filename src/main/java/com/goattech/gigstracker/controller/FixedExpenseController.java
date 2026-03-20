package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.FixedExpenseDto;
import com.goattech.gigstracker.service.FixedExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fixed-expenses")
@Tag(name = "Fixed Expenses", description = "Endpoints for managing user fixed expenses")
public class FixedExpenseController {

    private final FixedExpenseService fixedExpenseService;

    public FixedExpenseController(FixedExpenseService fixedExpenseService) {
        this.fixedExpenseService = fixedExpenseService;
    }

    @GetMapping
    @Operation(summary = "Get all fixed expenses for the authenticated user")
    public List<FixedExpenseDto> getFixedExpenses(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.getFixedExpensesForUser(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new fixed expense for the authenticated user")
    public FixedExpenseDto createFixedExpense(@AuthenticationPrincipal Jwt jwt, @RequestBody FixedExpenseDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.createFixedExpense(dto, userId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing fixed expense")
    public FixedExpenseDto updateFixedExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody FixedExpenseDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.updateFixedExpense(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a fixed expense")
    public ResponseEntity<Void> deleteFixedExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        fixedExpenseService.deleteFixedExpense(id, userId);
        return ResponseEntity.noContent().build();
    }
}
