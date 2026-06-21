package com.jam.ping.campsite.domain;

import com.jam.ping.regeion.district.code.DistrictCode;
import com.jam.ping.regeion.region.code.RegionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "region_code", nullable = false, length = 30)
    private RegionCode regionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "district_code", nullable = false, length = 50)
    private DistrictCode districtCode;

    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime;

    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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
