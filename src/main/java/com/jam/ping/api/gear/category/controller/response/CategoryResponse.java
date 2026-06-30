package com.jam.ping.api.gear.category.controller.response;

import com.jam.ping.api.gear.category.domain.Category;
import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById,
        String memo
) {

    /**
     * Category 엔티티를 관리자 응답 형식으로 변환합니다.
     */
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getCreatedById(),
                category.getUpdatedById(),
                category.getMemo()
        );
    }
}
