package com.jam.ping.recipe.main.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeRequest(
        @NotBlank(message = "레시피명은 필수입니다.")
        @Size(max = 100, message = "레시피명은 100자 이하여야 합니다.")
        String name,
        String ingredients,
        String instructions,
        @NotNull(message = "카테고리는 필수입니다.")
        Long recipeCategoryId
) {
}
