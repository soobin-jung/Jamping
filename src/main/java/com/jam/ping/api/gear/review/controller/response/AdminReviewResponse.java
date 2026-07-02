package com.jam.ping.api.gear.review.controller.response;

import com.jam.ping.api.gear.review.dto.ReviewDto;
import java.time.LocalDateTime;

public record AdminReviewResponse(
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

    public static AdminReviewResponse from(ReviewDto dto) {
        return new AdminReviewResponse(
                dto.id(),
                dto.gearId(),
                dto.userId(),
                dto.nickname(),
                dto.profileImageUrl(),
                dto.rating(),
                dto.content(),
                dto.status(),
                dto.moderationReason(),
                dto.moderatedBy(),
                dto.moderatedAt(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
