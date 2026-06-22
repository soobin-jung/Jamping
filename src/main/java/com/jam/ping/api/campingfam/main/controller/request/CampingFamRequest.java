package com.jam.ping.api.campingfam.main.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CampingFamRequest(
        @NotBlank(message = "캠핑팸 이름은 필수입니다.")
        @Size(max = 100, message = "캠핑팸 이름은 100자 이하여야 합니다.")
        String name,
        @NotNull(message = "캠핑장은 필수입니다.")
        Long campSiteId,
        @NotBlank(message = "예약 사이트는 필수입니다.")
        @Size(max = 255, message = "예약 사이트는 255자 이하여야 합니다.")
        String reservationSites
) {}
