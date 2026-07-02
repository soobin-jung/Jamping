package com.jam.ping.api.campingfam.schedule.dto;

import com.jam.ping.api.campingfam.schedule.domain.CampingFamScheduleTemp;
import java.time.LocalDate;

public record CampingFamScheduleTempDto(
        Long id,
        Long campingFamId,
        Long userId,
        LocalDate selectedDate
) {

    public static CampingFamScheduleTempDto from(CampingFamScheduleTemp temp) {
        return new CampingFamScheduleTempDto(
                temp.getId(),
                temp.getCampingFam().getId(),
                temp.getUserId(),
                temp.getSelectedDate()
        );
    }
}
