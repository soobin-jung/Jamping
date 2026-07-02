package com.jam.ping.api.gear.main.dto;

import com.jam.ping.api.gear.main.domain.Gear;
import java.time.LocalDateTime;

public record GearDto(
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

    public static GearDto from(Gear gear) {
        return new GearDto(
                gear.getId(),
                gear.getName(),
                gear.getLink(),
                gear.getImageUrl(),
                gear.getCategory().getId(),
                gear.getCategory().getName(),
                gear.getMaker().getId(),
                gear.getMaker().getName(),
                gear.getCreatedById(),
                gear.getCreatedAt(),
                gear.getUpdatedById(),
                gear.getUpdatedAt(),
                gear.getMemo()
        );
    }
}
