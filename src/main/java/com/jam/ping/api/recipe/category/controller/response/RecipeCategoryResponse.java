package com.jam.ping.api.recipe.category.controller.response;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import java.time.LocalDateTime;

public record RecipeCategoryResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById
) {

    public static RecipeCategoryResponse from(RecipeCategory recipeCategory) {
        return new RecipeCategoryResponse(
                recipeCategory.getId(),
                recipeCategory.getName(),
                recipeCategory.getCreatedAt(),
                recipeCategory.getUpdatedAt(),
                recipeCategory.getCreatedById(),
                recipeCategory.getUpdatedById()
        );
    }
}
