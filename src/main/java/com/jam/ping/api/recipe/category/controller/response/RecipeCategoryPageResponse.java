package com.jam.ping.api.recipe.category.controller.response;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import java.util.List;
import org.springframework.data.domain.Page;

public record RecipeCategoryPageResponse(
        List<RecipeCategoryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        String keyword
) {

    public static RecipeCategoryPageResponse from(Page<RecipeCategory> page, String keyword) {
        return new RecipeCategoryPageResponse(
                page.getContent().stream().map(RecipeCategoryResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                keyword
        );
    }
}
