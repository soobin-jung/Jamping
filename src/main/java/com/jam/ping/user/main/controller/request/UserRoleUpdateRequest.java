package com.jam.ping.user.main.controller.request;

import com.jam.ping.user.main.code.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
        @NotNull(message = "변경할 권한은 필수입니다.")
        UserRole role
) {
}
