package com.jam.ping.api.gear.main.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GearRequest(
        @NotBlank(message = "용품명은 필수입니다.")
        @Size(max = 100, message = "용품명은 100자 이하여야 합니다.")
        String name,
        @Size(max = 500, message = "링크는 500자 이하여야 합니다.")
        String link,
        @Size(max = 1000, message = "이미지 주소는 1000자 이하여야 합니다.")
        String imageUrl,
        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,
        @NotNull(message = "메이커는 필수입니다.")
        Long makerId,
        @Size(max = 1000, message = "메모는 1000자 이하여야 합니다.")
        String memo
) {
}
