package com.jam.ping.api.gear.category.controller.response;

import com.jam.ping.api.gear.category.dto.CategoryDto;
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

    public static CategoryResponse from(CategoryDto dto) {
        return new CategoryResponse(
                dto.id(), dto.name(), dto.createdAt(), dto.updatedAt(),
                dto.createdById(), dto.updatedById(), dto.memo()
        );
    }
}
