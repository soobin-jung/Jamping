package com.jam.ping.api.recipe.category.dto;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import java.time.LocalDateTime;

public record RecipeCategoryDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById
) {

    public static RecipeCategoryDto from(RecipeCategory recipeCategory) {
        return new RecipeCategoryDto(
                recipeCategory.getId(),
                recipeCategory.getName(),
                recipeCategory.getCreatedAt(),
                recipeCategory.getUpdatedAt(),
                recipeCategory.getCreatedById(),
                recipeCategory.getUpdatedById()
        );
    }
}
