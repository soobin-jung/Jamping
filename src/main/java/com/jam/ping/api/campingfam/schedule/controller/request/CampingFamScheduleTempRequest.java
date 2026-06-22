package com.jam.ping.api.campingfam.schedule.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CampingFamScheduleTempRequest(
        @NotEmpty(message = "날짜를 1개 이상 선택해야 합니다.")
        List<@NotNull LocalDate> dates
) {}
