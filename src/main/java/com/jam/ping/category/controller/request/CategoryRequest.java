package com.jam.ping.category.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "카테고리명은 필수입니다.")
        @Size(max = 100, message = "카테고리명은 100자 이하여야 합니다.")
        String name,
        @Size(max = 1000, message = "메모는 1000자 이하여야 합니다.")
        String memo
) {
}
