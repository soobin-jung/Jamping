package com.jam.ping.api.user.main.controller.response;

import com.jam.ping.api.user.main.domain.User;
import java.time.LocalDateTime;

public record UserListItemResponse(
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
     * User 엔티티를 관리자 목록 행 응답 형식으로 변환합니다.
     */
    public static UserListItemResponse from(User user) {
        return new UserListItemResponse(
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
