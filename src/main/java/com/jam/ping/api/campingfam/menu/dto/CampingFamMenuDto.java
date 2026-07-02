package com.jam.ping.api.campingfam.menu.dto;

import com.jam.ping.api.campingfam.menu.code.MealType;
import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import java.time.LocalDate;

public record CampingFamMenuDto(
        Long id,
        Long campingFamId,
        LocalDate campingDate,
        MealType mealType,
        String mealTypeLabel,
        String menu
) {

    public static CampingFamMenuDto from(CampingFamMenu campingFamMenu) {
        return new CampingFamMenuDto(
                campingFamMenu.getId(),
                campingFamMenu.getCampingFam().getId(),
                campingFamMenu.getCampingDate(),
                campingFamMenu.getMealType(),
                campingFamMenu.getMealType().getLabel(),
                campingFamMenu.getMenu()
        );
    }
}
