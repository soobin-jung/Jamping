package com.jam.ping.api.campingfam.main.domain;

import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private CampSite campSite;

    @Column(nullable = false, length = 255)
    private String reservationSites;

    @Column(nullable = true)
    private LocalDate campingStartDate;

    @Column(nullable = true)
    private LocalDate campingEndDate;

    public static CampingFam create(String name, CampSite campSite, String reservationSites) {
        CampingFam campingFam = new CampingFam();
        campingFam.name = name;
        campingFam.campSite = campSite;
        campingFam.reservationSites = reservationSites;
        return campingFam;
    }

    public void updateSchedule(LocalDate startDate, LocalDate endDate) {
        this.campingStartDate = startDate;
        this.campingEndDate = endDate;
    }
}
