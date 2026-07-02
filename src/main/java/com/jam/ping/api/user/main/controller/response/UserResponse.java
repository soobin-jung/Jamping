package com.jam.ping.api.user.main.controller.response;

import com.jam.ping.api.user.main.dto.UserDto;
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

    public static UserResponse from(UserDto dto) {
        return new UserResponse(
                dto.id(),
                dto.provider(),
                dto.providerUserId(),
                dto.email(),
                dto.nickname(),
                dto.profileImageUrl(),
                dto.role(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
