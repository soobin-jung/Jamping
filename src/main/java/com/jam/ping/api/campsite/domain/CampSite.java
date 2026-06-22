package com.jam.ping.api.campsite.domain;

import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.api.regeion.district.code.DistrictCode;
import com.jam.ping.api.regeion.region.code.RegionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampSite extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RegionCode regionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DistrictCode districtCode;

    @Column(nullable = false)
    private LocalTime checkInTime;

    @Column(nullable = false)
    private LocalTime checkOutTime;

    @Builder
    private CampSite(
            String name,
            String link,
            RegionCode regionCode,
            DistrictCode districtCode,
            LocalTime checkInTime,
            LocalTime checkOutTime
    ) {
        this.name = name;
        this.link = link;
        this.regionCode = regionCode;
        this.districtCode = districtCode;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void update(
            String name,
            String link,
            RegionCode regionCode,
            DistrictCode districtCode,
            LocalTime checkInTime,
            LocalTime checkOutTime
    ) {
        this.name = name;
        this.link = link;
        this.regionCode = regionCode;
        this.districtCode = districtCode;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
}
