package com.jam.ping.user.main.controller.response;

import com.jam.ping.user.main.domain.User;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String provider,
        String providerUserId,
        String email,
        String nickname,
        String profileImageUrl,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * User 엔티티를 관리자 상세 응답 형식으로 변환합니다.
     */
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getProvider().name(),
                user.getProviderUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
