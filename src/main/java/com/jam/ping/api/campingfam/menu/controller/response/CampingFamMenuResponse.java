package com.jam.ping.api.campingfam.menu.controller.response;

import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import java.time.LocalDate;

public record CampingFamMenuResponse(
        Long id,
        Long campingFamId,
        LocalDate campingDate,
        String breakfast,
        String lunch,
        String dinner,
        String snack
) {

    public static CampingFamMenuResponse from(CampingFamMenu menu) {
        return new CampingFamMenuResponse(
                menu.getId(),
                menu.getCampingFam().getId(),
                menu.getCampingDate(),
                menu.getBreakfast(),
                menu.getLunch(),
                menu.getDinner(),
                menu.getSnack()
        );
    }
}
