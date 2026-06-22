package com.jam.ping.api.campingfam.schedule.controller.response;

import com.jam.ping.api.campingfam.schedule.domain.CampingFamScheduleTemp;
import java.time.LocalDate;

public record CampingFamScheduleTempResponse(
        Long id,
        Long campingFamId,
        Long userId,
        LocalDate selectedDate
) {

    public static CampingFamScheduleTempResponse from(CampingFamScheduleTemp temp) {
        return new CampingFamScheduleTempResponse(
                temp.getId(),
                temp.getCampingFam().getId(),
                temp.getUserId(),
                temp.getSelectedDate()
        );
    }
}
