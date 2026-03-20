package com.goattech.gigstracker.controller;

import com.goattech.gigstracker.model.dto.AppDto;
import com.goattech.gigstracker.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints for managing gig worker applications")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping
    @Operation(summary = "Get all apps for the authenticated user")
    public List<AppDto> getApps(@AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return appService.getApps(userId, activeOnly);
    }

    @PostMapping
    @Operation(summary = "Create a new app for the authenticated user")
    public AppDto createApp(@AuthenticationPrincipal Jwt jwt, @RequestBody AppDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return appService.createApp(dto, userId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing app")
    public AppDto updateApp(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id, @RequestBody AppDto dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return appService.updateApp(id, dto, userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an app")
    public ResponseEntity<Void> deleteApp(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        appService.deleteApp(id, userId);
        return ResponseEntity.noContent().build();
    }
}
