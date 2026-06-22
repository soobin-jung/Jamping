package com.jam.ping.campingfam.main.domain;

import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFam extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long campSiteId;

    @Column(nullable = false, length = 255)
    private String reservationSites;

    @Column(nullable = false)
    private LocalDate campingStartDate;

    @Column(nullable = false)
    private LocalDate campingEndDate;

    @Builder
    private CampingFam(String name, Long campSiteId, String reservationSites, LocalDate campingStartDate, LocalDate campingEndDate) {
        this.name = name;
        this.campSiteId = campSiteId;
        this.reservationSites = reservationSites;
        this.campingStartDate = campingStartDate;
        this.campingEndDate = campingEndDate;
    }
}
