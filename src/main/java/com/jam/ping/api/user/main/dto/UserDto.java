package com.jam.ping.api.user.main.dto;

import com.jam.ping.api.user.main.domain.User;
import java.time.LocalDateTime;

public record UserDto(
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

    public static UserDto from(User user) {
        return new UserDto(
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
