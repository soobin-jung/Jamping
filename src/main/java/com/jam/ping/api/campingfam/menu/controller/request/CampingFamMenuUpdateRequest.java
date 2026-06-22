package com.jam.ping.api.campingfam.menu.controller.request;

import jakarta.validation.constraints.Size;

public record CampingFamMenuUpdateRequest(
        @Size(max = 200, message = "아침 메뉴는 200자 이하여야 합니다.")
        String breakfast,
        @Size(max = 200, message = "점심 메뉴는 200자 이하여야 합니다.")
        String lunch,
        @Size(max = 200, message = "저녁 메뉴는 200자 이하여야 합니다.")
        String dinner,
        @Size(max = 200, message = "간식 메뉴는 200자 이하여야 합니다.")
        String snack
) {}
