package com.jam.ping.api.recipe.category.controller.response;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import java.time.LocalDateTime;

public record RecipeCategoryResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {

    public static RecipeCategoryResponse from(RecipeCategory recipeCategory) {
        return new RecipeCategoryResponse(
                recipeCategory.getId(),
                recipeCategory.getName(),
                recipeCategory.getCreatedAt(),
                recipeCategory.getUpdatedAt(),
                recipeCategory.getCreatedBy() == null ? null : recipeCategory.getCreatedBy().getNickname(),
                recipeCategory.getUpdatedBy() == null ? null : recipeCategory.getUpdatedBy().getNickname()
        );
    }
}
