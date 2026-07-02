package com.jam.ping.api.campingfam.schedule.controller.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record
CampingFamFinalizeScheduleRequest(
        @NotNull(message = "캠핑 시작일은 필수입니다.")
        LocalDate campingStartDate,
        @NotNull(message = "캠핑 종료일은 필수입니다.")
        LocalDate campingEndDate
) {}
