package com.jam.ping.review.controller.response;

import com.jam.ping.review.domain.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long gearId,
        Long userId,
        String nickname,
        String profileImageUrl,
        Integer rating,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine
) {

    public static ReviewResponse from(Review review, Long actorUserId) {
        Long reviewUserId = review.getUser().getId();

        return new ReviewResponse(
                review.getId(),
                review.getGear().getId(),
                reviewUserId,
                review.getUser().getNickname(),
                review.getUser().getProfileImageUrl(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                actorUserId != null && actorUserId.equals(reviewUserId)
        );
    }
}
