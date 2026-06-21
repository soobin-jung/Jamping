package com.jam.ping.user.gear.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserGearRequest(
        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,
        Long makerId,
        Long gearId,
        @Size(max = 100, message = "장비명은 100자 이하여야 합니다.")
        String name,
        @Size(max = 1000, message = "비고는 1000자 이하여야 합니다.")
        String memo
) {
}
