package com.jam.ping.api.gear.main.controller.response;

import com.jam.ping.api.gear.main.domain.Gear;
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

    /**
     * Gear 엔티티를 관리자 응답 형식으로 변환합니다.
     */
    public static GearResponse from(Gear gear) {
        return new GearResponse(
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
