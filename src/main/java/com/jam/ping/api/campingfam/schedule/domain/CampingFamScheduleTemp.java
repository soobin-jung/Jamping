package com.jam.ping.api.campingfam.schedule.domain;

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
public class CampingFamScheduleTemp extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate selectedDate;

    @Builder
    private CampingFamScheduleTemp(CampingFam campingFam, Long userId, LocalDate selectedDate) {
        this.campingFam = campingFam;
        this.userId = userId;
        this.selectedDate = selectedDate;
    }
}
