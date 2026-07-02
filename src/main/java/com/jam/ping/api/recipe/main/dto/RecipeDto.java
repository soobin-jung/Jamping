package com.jam.ping.api.recipe.main.dto;

import com.jam.ping.api.recipe.main.domain.Recipe;
import java.time.LocalDateTime;

public record RecipeDto(
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

    public static RecipeDto from(Recipe recipe) {
        return new RecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getRecipeCategory().getId(),
                recipe.getRecipeCategory().getName(),
                recipe.getCreatedById(),
                recipe.getCreatedAt(),
                recipe.getUpdatedById(),
                recipe.getUpdatedAt()
        );
    }
}
