package com.jam.ping.user.main.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.user.main.code.UserRole;
import com.jam.ping.user.main.controller.request.UserRoleUpdateRequest;
import com.jam.ping.user.main.controller.response.UserPageResponse;
import com.jam.ping.user.main.controller.response.UserResponse;
import com.jam.ping.user.main.domain.User;
import com.jam.ping.user.main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public ApiRes<UserPageResponse> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) UserRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<UserPageResponse>()
                .successData(UserPageResponse.from(
                        userService.getUsers(keyword, role, page, size),
                        keyword == null ? "" : keyword.trim(),
                        role
                ));
    }

    @GetMapping("/{userId}")
    public ApiRes<UserResponse> getUser(@PathVariable Long userId) {
        return new ApiRes<UserResponse>()
                .successData(UserResponse.from(userService.getUser(userId)));
    }

    @PutMapping("/{userId}/role")
    public ApiRes<UserResponse> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleUpdateRequest request
    ) {
        User user = userService.updateUserRole(userId, request.role());

        return new ApiRes<UserResponse>()
                .successData(UserResponse.from(user))
                .manipulationOne(user.getId())
                .responseMsg("사용자 권한이 변경되었습니다.");
    }
}
