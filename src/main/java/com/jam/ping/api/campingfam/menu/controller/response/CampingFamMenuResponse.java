package com.jam.ping.api.campingfam.menu.controller.response;

import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import com.jam.ping.api.campingfam.menu.code.MealType;
import java.time.LocalDate;

public record CampingFamMenuResponse(
        Long id,
        Long campingFamId,
        LocalDate campingDate,
        MealType mealType,
        String mealTypeLabel,
        String menu
) {

    public static CampingFamMenuResponse from(CampingFamMenu campingFamMenu) {
        return new CampingFamMenuResponse(
                campingFamMenu.getId(),
                campingFamMenu.getCampingFam().getId(),
                campingFamMenu.getCampingDate(),
                campingFamMenu.getMealType(),
                campingFamMenu.getMealType().getLabel(),
                campingFamMenu.getMenu()
        );
    }
}
