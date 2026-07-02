package com.jam.ping.api.gear.review.controller.response;

import com.jam.ping.api.gear.review.dto.ReviewDto;
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

    public static ReviewResponse from(ReviewDto dto, Long actorUserId) {
        return new ReviewResponse(
                dto.id(),
                dto.gearId(),
                dto.userId(),
                dto.nickname(),
                dto.profileImageUrl(),
                dto.rating(),
                dto.content(),
                dto.createdAt(),
                dto.updatedAt(),
                actorUserId != null && actorUserId.equals(dto.userId())
        );
    }
}
