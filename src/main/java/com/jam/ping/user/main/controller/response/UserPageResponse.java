package com.jam.ping.user.main.controller.response;

import com.jam.ping.user.main.code.UserRole;
import com.jam.ping.user.main.domain.User;
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

    /**
     * Page<User>를 관리자 사용자 목록 응답 형식으로 변환합니다.
     */
    public static UserPageResponse from(Page<User> userPage, String keyword, UserRole role) {
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
