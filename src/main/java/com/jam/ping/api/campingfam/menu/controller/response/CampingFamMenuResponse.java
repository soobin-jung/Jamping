package com.jam.ping.api.campingfam.menu.controller.response;

import com.jam.ping.api.campingfam.menu.code.MealType;
import com.jam.ping.api.campingfam.menu.dto.CampingFamMenuDto;
import java.time.LocalDate;

public record CampingFamMenuResponse(
        Long id,
        Long campingFamId,
        LocalDate campingDate,
        MealType mealType,
        String mealTypeLabel,
        String menu
) {

    public static CampingFamMenuResponse from(CampingFamMenuDto dto) {
        return new CampingFamMenuResponse(
                dto.id(),
                dto.campingFamId(),
                dto.campingDate(),
                dto.mealType(),
                dto.mealTypeLabel(),
                dto.menu()
        );
    }
}
