package com.jam.ping.api.user.main.controller.request;

import com.jam.ping.api.user.main.code.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
        @NotNull(message = "변경할 권한은 필수입니다.")
        UserRole role
) {
}
