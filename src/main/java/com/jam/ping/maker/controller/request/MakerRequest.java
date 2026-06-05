package com.jam.ping.maker.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MakerRequest(
        @NotBlank(message = "메이커명은 필수입니다.")
        @Size(max = 100, message = "메이커명은 100자 이하여야 합니다.")
        String name,
        @Size(max = 100, message = "영문 메이커명은 100자 이하여야 합니다.")
        String nameEng,
        @Size(max = 500, message = "홈페이지 주소는 500자 이하여야 합니다.")
        String homepageUrl
) {
}
