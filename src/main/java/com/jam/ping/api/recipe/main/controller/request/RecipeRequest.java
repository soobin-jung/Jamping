package com.jam.ping.api.recipe.main.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeRequest(
        @NotBlank(message = "레시피명은 필수입니다.")
        @Size(max = 100, message = "레시피명은 100자 이하여야 합니다.")
        String name,
        @Size(max = 5000, message = "재료는 5000자 이하여야 합니다.")
        String ingredients,
        @Size(max = 10000, message = "조리법은 10000자 이하여야 합니다.")
        String instructions,
        @NotNull(message = "카테고리는 필수입니다.")
        Long recipeCategoryId
) {
}
