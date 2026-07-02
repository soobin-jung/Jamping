package com.jam.ping.api.gear.review.dto;

import com.jam.ping.api.gear.review.domain.Review;
import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Long gearId,
        Long userId,
        String nickname,
        String profileImageUrl,
        Integer rating,
        String content,
        String status,
        String moderationReason,
        String moderatedBy,
        LocalDateTime moderatedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReviewDto from(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getGear().getId(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getUser().getProfileImageUrl(),
                review.getRating(),
                review.getContent(),
                review.getStatus().name(),
                review.getModerationReason(),
                review.getModeratedBy() == null ? null : review.getModeratedBy().getNickname(),
                review.getModeratedAt(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
