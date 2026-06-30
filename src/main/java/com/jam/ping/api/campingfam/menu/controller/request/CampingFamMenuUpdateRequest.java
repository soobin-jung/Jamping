package com.jam.ping.api.campingfam.menu.controller.request;

import jakarta.validation.constraints.Size;

public record CampingFamMenuUpdateRequest(
        @Size(max = 200, message = "메뉴는 200자 이하여야 합니다.")
        String menu
) {}
