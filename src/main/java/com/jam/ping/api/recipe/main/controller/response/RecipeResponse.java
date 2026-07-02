package com.jam.ping.api.recipe.main.controller.response;

import com.jam.ping.api.recipe.main.dto.RecipeDto;
import java.time.LocalDateTime;

public record RecipeResponse(
        Long id,
        String name,
        String ingredients,
        String instructions,
        Long recipeCategoryId,
        String recipeCategoryName,
        Long createdById,
        LocalDateTime createdAt,
        Long updatedById,
        LocalDateTime updatedAt
) {

    public static RecipeResponse from(RecipeDto dto) {
        return new RecipeResponse(
                dto.id(),
                dto.name(),
                dto.ingredients(),
                dto.instructions(),
                dto.recipeCategoryId(),
                dto.recipeCategoryName(),
                dto.createdById(),
                dto.createdAt(),
                dto.updatedById(),
                dto.updatedAt()
        );
    }
}
