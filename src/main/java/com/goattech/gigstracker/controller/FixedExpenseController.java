package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.FixedExpenseDto;
import com.goattech.gigstracker.service.FixedExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fixed-expenses")
public class FixedExpenseController {

    private final FixedExpenseService fixedExpenseService;

    public FixedExpenseController(FixedExpenseService fixedExpenseService) {
        this.fixedExpenseService = fixedExpenseService;
    }

    @GetMapping
    public List<FixedExpenseDto> getFixedExpenses(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.getFixedExpensesForUser(userId);
    }

    @PostMapping
    public FixedExpenseDto createFixedExpense(@AuthenticationPrincipal Jwt jwt, @RequestBody FixedExpenseDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.createFixedExpense(dto, userId);
    }

    @PutMapping("/{id}")
    public FixedExpenseDto updateFixedExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody FixedExpenseDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return fixedExpenseService.updateFixedExpense(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFixedExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        fixedExpenseService.deleteFixedExpense(id, userId);
        return ResponseEntity.noContent().build();
    }
}
