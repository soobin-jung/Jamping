package com.jam.ping.review.controller.response;

import com.jam.ping.review.domain.Review;
import java.util.List;
import org.springframework.data.domain.Page;

public record ReviewPageResponse(
        List<ReviewResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static ReviewPageResponse from(Page<Review> reviewPage, Long actorUserId) {
        return new ReviewPageResponse(
                reviewPage.getContent().stream()
                        .map(review -> ReviewResponse.from(review, actorUserId))
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
