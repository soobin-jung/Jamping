package com.jam.ping.api.recipe.main.controller.response;

import com.jam.ping.api.recipe.main.domain.Recipe;
import java.util.List;
import org.springframework.data.domain.Page;

public record RecipePageResponse(
        List<RecipeResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        Long recipeCategoryId,
        String keyword
) {

    public static RecipePageResponse from(Page<Recipe> recipePage, Long recipeCategoryId, String keyword) {
        return new RecipePageResponse(
                recipePage.getContent().stream().map(RecipeResponse::from).toList(),
                recipePage.getNumber(),
                recipePage.getSize(),
                recipePage.getTotalElements(),
                recipePage.getTotalPages(),
                recipePage.hasNext(),
                recipePage.hasPrevious(),
                recipeCategoryId,
                keyword
        );
    }
}
