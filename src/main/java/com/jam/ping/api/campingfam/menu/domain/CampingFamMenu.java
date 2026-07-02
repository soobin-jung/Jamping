package com.jam.ping.api.campingfam.menu.domain;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.menu.code.MealType;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamMenu extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @Column(nullable = false)
    private LocalDate campingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MealType mealType;

    @Column(length = 200)
    private String menu;

    public static CampingFamMenu create(CampingFam campingFam, LocalDate campingDate, MealType mealType) {
        CampingFamMenu menu = new CampingFamMenu();
        menu.campingFam = campingFam;
        menu.campingDate = campingDate;
        menu.mealType = mealType;
        return menu;
    }

    public void update(String menu) {
        this.menu = menu;
    }
}

