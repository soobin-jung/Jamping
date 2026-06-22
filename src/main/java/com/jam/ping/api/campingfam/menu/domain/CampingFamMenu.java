package com.jam.ping.api.campingfam.menu.domain;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
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

    @Column(length = 200)
    private String breakfast;

    @Column(length = 200)
    private String lunch;

    @Column(length = 200)
    private String dinner;

    @Column(length = 200)
    private String snack;

    @Builder
    private CampingFamMenu(CampingFam campingFam, LocalDate campingDate) {
        this.campingFam = campingFam;
        this.campingDate = campingDate;
    }

    public void update(String breakfast, String lunch, String dinner, String snack) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
    }
}
