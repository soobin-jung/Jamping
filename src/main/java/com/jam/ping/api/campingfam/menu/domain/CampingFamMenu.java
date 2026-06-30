package com.jam.ping.api.campingfam.menu.domain;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.menu.code.MealType;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    private CampingFamMenu(CampingFam campingFam, LocalDate campingDate, MealType mealType) {
        this.campingFam = campingFam;
        this.campingDate = campingDate;
        this.mealType = mealType;
    }

    public void update(String menu) {
        this.menu = menu;
    }
}

