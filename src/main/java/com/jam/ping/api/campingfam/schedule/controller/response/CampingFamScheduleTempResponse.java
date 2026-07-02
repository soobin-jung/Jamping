package com.jam.ping.api.campingfam.schedule.controller.response;

import com.jam.ping.api.campingfam.schedule.dto.CampingFamScheduleTempDto;
import java.time.LocalDate;

public record CampingFamScheduleTempResponse(
        Long id,
        Long campingFamId,
        Long userId,
        LocalDate selectedDate
) {

    public static CampingFamScheduleTempResponse from(CampingFamScheduleTempDto dto) {
        return new CampingFamScheduleTempResponse(
                dto.id(),
                dto.campingFamId(),
                dto.userId(),
                dto.selectedDate()
        );
    }
}
