package com.jam.ping.category.controller.response;

import com.jam.ping.category.domain.Category;
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

    /**
     * Page<Category>를 관리자 목록 응답 형식으로 변환합니다.
     */
    public static CategoryPageResponse from(Page<Category> categoryPage, String keyword) {
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
