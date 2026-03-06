package com.goattech.gigstracker.service;

import com.goattech.gigstracker.exception.BusinessException;
import com.goattech.gigstracker.model.App;
import com.goattech.gigstracker.model.dto.AppDto;
import com.goattech.gigstracker.repository.AppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppService {

    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public List<AppDto> getApps(UUID userId, boolean activeOnly) {
        List<App> apps;
        if (activeOnly) {
            apps = appRepository.findByUserIdAndActiveTrue(userId);
        } else {
            apps = appRepository.findByUserId(userId);
        }
        return apps.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AppDto createApp(AppDto dto, UUID userId) {
        App app = new App();
        app.setName(dto.name());
        app.setUserId(userId);
        app.setActive(dto.active() != null ? dto.active() : true);
        return toDto(appRepository.save(app));
    }

    @Transactional
    public AppDto updateApp(UUID id, AppDto dto, UUID userId) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new BusinessException("App not found"));

        if (!app.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        if (dto.name() != null) {
            app.setName(dto.name());
        }
        if (dto.active() != null) {
            app.setActive(dto.active());
        }

        return toDto(appRepository.save(app));
    }

    @Transactional
    public void deleteApp(UUID id, UUID userId) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new BusinessException("App not found"));

        if (!app.getUserId().equals(userId)) {
            throw new BusinessException("Access denied");
        }

        appRepository.delete(app);
    }

    private AppDto toDto(App entity) {
        return new AppDto(
                entity.getId(),
                entity.getName(),
                entity.getActive(),
                entity.getCreatedAt());
    }
}
