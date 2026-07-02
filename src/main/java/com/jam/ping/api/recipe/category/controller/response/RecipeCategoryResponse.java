package com.jam.ping.api.recipe.category.controller.response;

import com.jam.ping.api.recipe.category.dto.RecipeCategoryDto;
import java.time.LocalDateTime;

public record RecipeCategoryResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById
) {

    public static RecipeCategoryResponse from(RecipeCategoryDto dto) {
        return new RecipeCategoryResponse(
                dto.id(),
                dto.name(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.createdById(),
                dto.updatedById()
        );
    }
}
