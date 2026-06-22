package com.jam.ping.api.user.main.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.api.user.main.controller.request.UserRoleUpdateRequest;
import com.jam.ping.api.user.main.controller.request.UserSearchRequest;
import com.jam.ping.api.user.main.controller.response.UserPageResponse;
import com.jam.ping.api.user.main.controller.response.UserResponse;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.api.user.main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public ApiRes<UserPageResponse> getUsers(@Valid @ModelAttribute UserSearchRequest request) {
        return new ApiRes<UserPageResponse>()
                .successData(UserPageResponse.from(
                        userService.getUsers(request.keyword(), request.role(), request.page(), request.size()),
                        request.keyword().trim(),
                        request.role()
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
