package com.jam.ping.api.gear.category.controller.response;

import com.jam.ping.api.gear.category.dto.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record CategoryPageResponse(
        List<CategoryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        String keyword
) {

    public static CategoryPageResponse from(Page<CategoryDto> categoryPage, String keyword) {
        return new CategoryPageResponse(
                categoryPage.getContent().stream()
                        .map(CategoryResponse::from)
                        .toList(),
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.hasNext(),
                categoryPage.hasPrevious(),
                keyword
        );
    }
}
