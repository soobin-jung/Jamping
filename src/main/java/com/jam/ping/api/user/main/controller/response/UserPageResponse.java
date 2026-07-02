package com.jam.ping.api.user.main.controller.response;

import com.jam.ping.api.user.main.code.UserRole;
import com.jam.ping.api.user.main.dto.UserDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record UserPageResponse(
        List<UserListItemResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        String keyword,
        String role
) {

    public static UserPageResponse from(Page<UserDto> userPage, String keyword, UserRole role) {
        return new UserPageResponse(
                userPage.getContent().stream()
                        .map(UserListItemResponse::from)
                        .toList(),
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.hasNext(),
                userPage.hasPrevious(),
                keyword,
                role == null ? null : role.name()
        );
    }
}
