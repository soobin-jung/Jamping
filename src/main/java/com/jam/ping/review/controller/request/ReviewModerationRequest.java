package com.jam.ping.review.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewModerationRequest(
        @NotBlank(message = "비활성화 사유는 필수입니다.")
        @Size(max = 1000, message = "비활성화 사유는 1000자 이하여야 합니다.")
        String moderationReason
) {
}
