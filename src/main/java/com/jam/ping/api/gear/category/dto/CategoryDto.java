package com.jam.ping.api.gear.category.dto;

import com.jam.ping.api.gear.category.domain.Category;
import java.time.LocalDateTime;

public record CategoryDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById,
        String memo
) {

    public static CategoryDto from(Category category) {
        return new CategoryDto(
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
