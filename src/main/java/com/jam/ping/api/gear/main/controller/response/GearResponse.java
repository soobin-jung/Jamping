package com.jam.ping.api.gear.main.controller.response;

import com.jam.ping.api.gear.main.dto.GearDto;
import java.time.LocalDateTime;

public record GearResponse(
        Long id,
        String name,
        String link,
        String imageUrl,
        Long categoryId,
        String categoryName,
        Long makerId,
        String makerName,
        Long createdById,
        LocalDateTime createdAt,
        Long updatedById,
        LocalDateTime updatedAt,
        String memo
) {

    public static GearResponse from(GearDto dto) {
        return new GearResponse(
                dto.id(),
                dto.name(),
                dto.link(),
                dto.imageUrl(),
                dto.categoryId(),
                dto.categoryName(),
                dto.makerId(),
                dto.makerName(),
                dto.createdById(),
                dto.createdAt(),
                dto.updatedById(),
                dto.updatedAt(),
                dto.memo()
        );
    }
}
