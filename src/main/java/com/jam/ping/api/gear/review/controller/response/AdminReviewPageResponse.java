package com.jam.ping.api.gear.review.controller.response;

import com.jam.ping.api.gear.review.dto.ReviewDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record AdminReviewPageResponse(
        List<AdminReviewResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static AdminReviewPageResponse from(Page<ReviewDto> reviewPage) {
        return new AdminReviewPageResponse(
                reviewPage.getContent().stream()
                        .map(AdminReviewResponse::from)
                        .toList(),
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.hasNext(),
                reviewPage.hasPrevious()
        );
    }
}
